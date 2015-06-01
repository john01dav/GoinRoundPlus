package src.john01dav.goinroundplus.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.goinroundplus.GoinRoundPlus;

/**
 * Abstract class to provide utilities used by many commands.
 * Right now, this class only provides basic error reporting and the ability to check if a sender is a player
 */
public abstract class CommandBase implements CommandExecutor{
    private GoinRoundPlus goinRoundPlus;
    private boolean requiresPlayer = false;

    protected CommandBase(GoinRoundPlus goinRoundPlus, String commandName){
        this.goinRoundPlus = goinRoundPlus;

        goinRoundPlus.getCommand(commandName).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        try{
            if((!(sender instanceof Player)) && requiresPlayer){
                sender.sendMessage(goinRoundPlus.getConfigManager().getMessage("playersonly"));
                return true;
            }

            return onCommand(sender, cmd, args);
        }catch(Throwable t){
            sender.sendMessage(ChatColor.DARK_RED + t.getClass().getName() + ": " + ChatColor.RED + t.getMessage());
            t.printStackTrace();
        }
        return true;
    }

    protected void setRequiresPlayer(boolean requiresPlayer){
        this.requiresPlayer = requiresPlayer;
    }

    /**
     * This method is called by the CommandBase class to be used as the onCommand() method for the command represented by the class that extends CommandBase
     * @param sender org.bukkit.command.CommandSender Who sent the command?
     * @param cmd org.bukkit.command.Command General information about the command.
     * @param args String[] List of arguments to the command
     * @return boolean Was the command called correctly? If true, nothing is done. If false, the player is shown the usage property in plugin.yml
     */
    protected abstract boolean onCommand(CommandSender sender, Command cmd, String[] args);

}
