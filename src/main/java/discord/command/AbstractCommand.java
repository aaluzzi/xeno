package discord.command;

import sx.blah.discord.handle.obj.IMessage;

public abstract class AbstractCommand {
    
    private final String[] names;
    private final int argsNeeded;
    private final CommandCategory category;
    
    public AbstractCommand(String[] names, int argsNeeded, CommandCategory category) {
        this.names = names;
        this.argsNeeded = argsNeeded;
        this.category = category;
    }
    
    public abstract void execute(IMessage message, String[] args);
    
    public abstract String getUsage(String alias);
      
    public String getName() {
        return names[0];
    }
    
    public String[] getNames() {
        return names;
    }    
    
    public int getArgsNeeded() {
        return argsNeeded;
    }
    
    public CommandCategory getCategory() {
        return category;
    }
}