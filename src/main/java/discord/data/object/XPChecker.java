package discord.data.object;

import discord.manager.UserManager;

import java.util.List;

import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.VoiceChannel;

public class XPChecker implements Runnable {

    private final Guild guild;
    private int saveCounter = 1;

    public XPChecker(Guild guild) {
        this.guild = guild;
    }

    public static boolean voiceStateIsNotTalking(VoiceState state) {
        return state.isMuted() || state.isDeaf() || state.isSelfMuted() || state.isSelfDeaf();
    }

    @Override
    public void run() {
        checkVoiceChannels();
    }

    private void checkVoiceChannels() {
        List<VoiceChannel> channels = guild.getChannels().ofType(VoiceChannel.class).collectList().block();
        channels.removeIf(channel -> channel.equals(guild.getAfkChannel().block())); //TODO solve this
        for (VoiceChannel channel : channels) {
            checkUsers(channel.getVoiceStates().collectList().block()); //tested it and it wasn't null with empty voice channels
        }
        if (saveCounter == 30) {
            System.out.println("Periodically saving user database");
            UserManager.saveDatabase();
            saveCounter = 0;
        } else {
            saveCounter++;
        }
    }

    private void checkUsers(List<VoiceState> states) {
        states.removeIf(state -> (state.getUser().block().isBot() || voiceStateIsNotTalking(state))); //only count real people that are "talking"
        int initialSize = states.size();
        if (states.size() >= 2) {
            states.removeIf(state -> UserManager.getDUserFromID(state.getUserId().asLong()).getProg().isMaxLevel());
            states.forEach(state -> UserManager.getDUserFromID(state.getUserId().asLong()).getProg().addPeriodicXP(initialSize));
        }
    }

}
