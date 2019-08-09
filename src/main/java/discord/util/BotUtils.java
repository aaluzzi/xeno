package discord.util;

import discord.core.command.CommandManager;
import discord.Main;
import discord.data.object.User;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class BotUtils {

    public static void sendEmbedMessage(IChannel channel, EmbedObject object) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(object);
            } catch (DiscordException e) {
                System.err.println("Message could not be sent with error: ");
                e.printStackTrace();
            }
        }).get();
    }

    public static void sendMessage(IChannel channel, String outside, String header, String body, Color color) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(outside, getBuilder(channel.getClient(), header, body, color).build());
            } catch (DiscordException e) {
                System.err.println("Message could not be sent with error: ");
                e.printStackTrace();
            }
        }).get(); //.get() makes sure they send in order cause async??
    }

    public static void sendMessage(IChannel channel, String header, String body, Color color) {
        sendMessage(channel, "", header, body, color);
    }

    public static void sendMessage(IChannel channel, String header, String body) {
        sendMessage(channel, header, body, Color.WHITE);
    }

    public static void sendInfoMessage(IChannel channel, String message) {
        sendMessage(channel, "Info", message, Color.GREEN);
    }

    public static void sendErrorMessage(IChannel channel, String message) {
        sendMessage(channel, "Error", message, Color.RED);
    }

    public static void sendUsageMessage(IChannel channel, String message) {
        sendMessage(channel, "Usage", message, Color.ORANGE);
    }

    public static void setUserNickname(IGuild guild, IUser user, String name) {
        RequestBuffer.request(() -> {
            try {
                guild.setUserNickname(user, name);
                System.out.println("Set nickname of " + user.getName() + " to " + name);
            } catch (DiscordException e) {
                System.err.println("Name could not be set with error: ");
                e.printStackTrace();
            }
        });
    }

    public static void setUserRoles(IGuild guild, IUser user, List<IRole> roles) {
        RequestBuffer.request(() -> {
            try {
                guild.editUserRoles(user, roles.toArray(new IRole[roles.size()]));
            } catch (DiscordException e) {
                System.err.println("Roles could not be set with error: ");
                e.printStackTrace();
            }
        });
    }

    public static void addMessageReaction(IMessage message, ReactionEmoji emoji) {
        RequestBuffer.request(() -> message.addReaction(emoji)).get();
    }

    public static void removeMessageReaction(IMessage message, IUser user, IReaction reaction) {
        RequestBuffer.request(() -> message.removeReaction(user, reaction));
    }

    public static void removeAllReactions(IMessage message) {
        RequestBuffer.request(() -> message.removeAllReactions());
    }

    public static void editMessage(IMessage message, String newText) {
        RequestBuffer.request(() -> message.edit(newText)).get();
    }

    public static void editMessage(IMessage message, EmbedObject embed) {
        RequestBuffer.request(() -> message.edit(embed)).get();
    }

    public static void editMessage(IMessage message, String content, EmbedObject embed) {
        RequestBuffer.request(() -> message.edit(content, embed)).get();
    }

    public static void deleteMessage(IMessage message) {
        RequestBuffer.request(() -> message.delete());
    }

    public static EmbedBuilder getBuilder(IDiscordClient client, String title, String desc, Color color) {
        return getBaseBuilder(client)
                .withAuthorName(title)
                .withDesc(desc)
                .withColor(color);
    }

    public static EmbedBuilder getBuilder(IDiscordClient client, String title, String desc) {
        return getBuilder(client, title, desc, Color.WHITE);
    }

    public static EmbedBuilder getBaseBuilder(IDiscordClient client) {
        return new EmbedBuilder().withAuthorIcon(client.getOurUser().getAvatarURL());
    }

    public static String getMention(User user) {
        return "<@!" + user.getDiscordID() + ">";
    }

    public static String buildUsage(String alias, String args, String desc) {
        return (String.format("`%s%s %s` \n\n%s", CommandManager.CMD_PREFIX, alias, args, desc));
    }

    //Takes a string and strips it of any non-basic characters and symbols
    public static String validateString(String string) {
        Matcher m = Pattern.compile("([\u0020-\u00FF]+)").matcher(string);
        String result = "";
        while (m.find()) {
            result += m.group(1);
        }
        return result;
    }

    public static String validateNick(String nick) {
        String result = validateString(nick);
        if (result.length() > 24) { //we dont want the nick too long
            result = result.substring(0, 23);
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