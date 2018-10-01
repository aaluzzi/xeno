package discord.object;

import discord.BotUtils;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class ProfileBuilder {
    
    EmbedBuilder builder; 
    User user;
    Progress progress;
    
    public ProfileBuilder(IGuild guild, User user) {
        this.user = user;
        this.builder = BotUtils.getBaseBuilder(guild.getClient());
        this.progress = user.getProgress();
        
        setupBase(guild);
    }
    
    private void setupBase(IGuild guild) {
        IUser dUser = guild.getUserByID(user.getID());
        builder.withAuthorName(user.getName());
        builder.withDesc(progress.getRank().getName());
        builder.withColor(dUser.getColorForGuild(guild));
        builder.withThumbnail(dUser.getAvatarURL());      
    }
    
    public ProfileBuilder addLevel() {
        builder.appendField("Level :gem:", "`" + progress.getLevel() + "`", true);
        return this;
    }
    
    public ProfileBuilder addXPProgress() {
        builder.appendField("XP :diamond_shape_with_a_dot_inside:", "`" 
                + progress.getXP() + "/" + progress.getXpTotalForLevelUp() + "`", true); 
        return this;
    }
    
    public ProfileBuilder addPrestige() {
        builder.appendField("Prestige :trophy:", "`"
                    + progress.getPrestige().getNumber() 
                    + progress.getPrestige().getBadge() + "`", true);
        return this;
    }
    
    public ProfileBuilder addBarProgressToNextLevel() {
        int percentage = (int) Math.round((double) progress.getXP() 
                / progress.getXpTotalForLevelUp() * 100); //percentage calc
        builder.appendField(percentage + "% to Level " + (progress.getLevel() + 1) 
                + " :chart_with_upwards_trend:", drawBarProgress(percentage), false);
        return this;
    }
    
    public ProfileBuilder addBarProgressToMaxLevel() {
        int currentTotalXP = getTotalXPToLevel(progress.getLevel()) + progress.getXP();
        int maxXP = getTotalXPToLevel(Progress.MAX_LEVEL);
        int percentage = (int) Math.round((double) currentTotalXP / maxXP * 100);
        builder.appendField(percentage + "% to Max Level :checkered_flag:", 
                drawBarProgress(percentage), false);
        return this;
    }
    
    public ProfileBuilder addTotalLevel() {
        builder.appendField("Total Level :arrows_counterclockwise:", 
                "`" + progress.getTotalLevels() + "`", true);
        return this;
    }
    
    public ProfileBuilder addTotalXP() {
        builder.appendField("Total XP :clock4:", 
                "`" + getTotalXP(progress) + "`", true);
        return this;
    }
    
    public ProfileBuilder addBadgeCase() {
        builder.appendField("Badge Case :beginner: ", "`" + getUserBadges(progress) + "`", true);
        return this;
    }
    
    public EmbedObject build() {
        return builder.build();
    }   
    
    private String getUserBadges(Progress progress) {
        String badges = "";
        for (int i = 1; i <= progress.getPrestige().getNumber(); i++) {
            badges += Prestige.BADGES[i];
        }
        return badges;
    } 
    
    private int getTotalXP(Progress progress) {
        int xp = 0;      
        for (int i = 0; i < progress.getPrestige().getNumber(); i++) {
            xp += getTotalXPToLevel(Progress.MAX_LEVEL);
        }
        xp += getTotalXPToLevel(progress.getLevel());
        xp += progress.getXP();
        return xp;
    }
       
    private int getTotalXPToLevel(int level) {
        int xp = 0;
        for (int i = 1; i < level; i++) {
            xp += i * 10 + 50; //hardcoded
        }
        return xp;
    }
     
    private String drawBarProgress(int percentage) {
        StringBuilder builder = new StringBuilder();
        //generate an int 1-10 depicting progress based on percentage
        int prog = percentage / 10;       
        for (int i = 1; i <= 10; i++) {
            if (i <= prog)
                builder.append(":white_large_square: ");
            else //different emojis handled by discord
                builder.append(":white_square_button: ");
        }
        return builder.toString();
    }
    
}