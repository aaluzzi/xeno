package discord.util;

import discord.core.command.CommandManager;
import discord.Main;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import discord.listener.EventsHandler;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildEmoji;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class BotUtils {

    public static String BOT_AVATAR_URL = "";

    public static Color getRandomColor() {
        return Color.of((int) (Math.random() * (0xFFFFFF + 1)));
    }

    public static TextChannel getGuildTextChannel(String name, Guild guild) {
        return guild.getChannels().ofType(TextChannel.class)
                .filter(channel -> channel.getName().equals(name))
                .collectList().block().get(0); //TODO this will blow up if channel doesn't exist
    }

    public static Role getGuildRole(String name, Guild guild) {
        return guild.getRoles().filter(role -> role.getName().equalsIgnoreCase(name)).collectList().block().get(0);
    }

    public static String getGuildEmojiString(Guild guild, String emojiName) {
        return guild.getClient().getGuildById(EventsHandler.THE_REALM_ID).flatMapMany(Guild::getEmojis)
                .filter(e -> e.getName().equals(emojiName)).map(GuildEmoji::asFormat).switchIfEmpty(Mono.just("")).blockFirst();
    }

    public static ReactionEmoji getGuildEmoji(Guild guild, String emojiName) {
        return ReactionEmoji.custom(guild.getClient().getGuildById(EventsHandler.THE_REALM_ID).flatMapMany(Guild::getEmojis)
                .filter(e -> e.getName().equals(emojiName)).blockFirst());
    }

    public static String getRandomGuildEmoji(Guild guild, String[] emojiNames) {
        return getGuildEmojiString(guild, emojiNames[(int) (Math.random() * emojiNames.length)]);
    }

    public static String buildUsage(String alias, String args, String desc) {
        return (String.format("`%s%s%s` \n\n%s", CommandManager.CMD_PREFIX, alias, args.isEmpty() ? "" : " " + args, desc));
    }

    //Takes a string and strips it of any non-basic characters and symbols
    public static String validateString(String string) {
        Matcher m = Pattern.compile("([!-~|¿-ȯ| ]+)").matcher(string);
        String result = "";
        while (m.find()) {
            result += m.group(1);
        }
        return result;
    }

    public static String validateNick(String nick) {
        String result = validateString(nick);
        if (result.length() > 30) { //cant have nick too long to conflict with prestige symbol
            result = result.substring(0, 30);
        }
        return result.trim(); //trim in case the string is cut off and the last char is a space
    }

    public static String getVersion() {
        try {
            final Properties properties = new Properties();
            properties.load(Main.class.getClassLoader().getResourceAsStream("project.properties"));
            return properties.getProperty("version");
        } catch (IOException e) {
            return "Version could not be retrieved with error " + e;
        }
    }


}
