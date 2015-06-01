package src.john01dav.goinroundplus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import src.john01dav.goinroundplus.GoinRoundPlus;
import src.john01dav.goinroundplus.teleport.TeleportTask;

public class CommandStartCycle extends CommandBase{
    private GoinRoundPlus goinRoundPlus;

    public CommandStartCycle(GoinRoundPlus goinRoundPlus){
        super(goinRoundPlus, "startcycle");
        setRequiresPlayer(true);

        this.goinRoundPlus = goinRoundPlus;
    }

    @Override
    protected boolean onCommand(CommandSender sender, Command cmd, String[] args){
        Player player = ((Player) sender);

        goinRoundPlus.getTeleportDispatcher().addTeleportTask(new TeleportTask(goinRoundPlus, player));

        player.sendMessage(goinRoundPlus.getConfigManager().getMessage("cyclestarted"));
        return true;
    }

}
