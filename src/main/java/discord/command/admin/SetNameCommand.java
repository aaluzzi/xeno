package discord.command.admin;

import discord.BotUtils;
import discord.CommandHandler;
import discord.NameManager;
import discord.UserManager;
import discord.command.AbstractCommand;
import discord.command.CommandCategory;
import discord.object.User;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class SetNameCommand extends AbstractCommand{
    
    public SetNameCommand() {
        super(new String[] {"setname", "changename"}, 2, CommandCategory.ADMIN);
    }
    
    public void execute(IMessage message, String[] args) {
        User userToChange = UserManager.getUserFromID(Long.parseLong(args[0]));
        IChannel channel = message.getChannel();
        if (userToChange == null) {
            BotUtils.sendErrorMessage(channel,
                    "Specified ID was not found in the database.");
            return;
        }
        args[1] = CommandHandler.combineArgs(1, args);
        NameManager.setNameOfUser(message.getGuild(), userToChange, args[1]);
        BotUtils.sendInfoMessage(channel, "Name set to " + args[1]);
        return;
    }
    
    public String getUsage(String alias) {
        return BotUtils.buildUsage(alias, "[userID] [new name]", 
                "Change the name of a user in the database."
                + "\n\nuserID - The user's long ID.");
    }
    
}