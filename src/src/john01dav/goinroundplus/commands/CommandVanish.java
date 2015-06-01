package src.john01dav.goinroundplus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.goinroundplus.GoinRoundPlus;

public class CommandVanish extends CommandBase{
    private GoinRoundPlus goinRoundPlus;

    public CommandVanish(GoinRoundPlus goinRoundPlus){
        super(goinRoundPlus, "vanish");
        setRequiresPlayer(true);

        this.goinRoundPlus = goinRoundPlus;
    }

    @Override
    protected boolean onCommand(CommandSender sender, Command cmd, String[] args){
        Player player = ((Player) sender);

        goinRoundPlus.getVanishHandler().setVanished(player, !goinRoundPlus.getVanishHandler().getVanished(player));

        if(goinRoundPlus.getVanishHandler().getVanished(player)){
            player.sendMessage(goinRoundPlus.getConfigManager().getMessage("vanished"));
        }else{
            player.sendMessage(goinRoundPlus.getConfigManager().getMessage("unvanished"));
        }

        return true;
    }

}
