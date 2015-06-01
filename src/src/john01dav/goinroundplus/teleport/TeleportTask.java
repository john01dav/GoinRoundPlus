package src.john01dav.goinroundplus.teleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import src.john01dav.goinroundplus.GoinRoundPlus;

import java.util.ArrayList;

/**
 * An instance of this class represents a cycle, usually still running.
 * All reference to instances of this class are removed by the plugin
 * shortly after the task ends in order to allow the instance to be
 * garbage collected
 */
public class TeleportTask {
    private GoinRoundPlus goinRoundPlus;

    private Player player;
    private Location originalLocation;
    private boolean wasEnableFly;
    private boolean wasFlying;
    private boolean wasVanished;

    private ArrayList<Player> teleportStops;
    private int currentIndex = 0;

    public TeleportTask(GoinRoundPlus goinRoundPlus, Player player){
        this.goinRoundPlus = goinRoundPlus;
        this.player = player;

        originalLocation = player.getLocation();
        wasEnableFly = player.getAllowFlight();
        wasFlying = player.isFlying();
        wasVanished = goinRoundPlus.getVanishHandler().getVanished(player);

        player.setAllowFlight(true);
        goinRoundPlus.getVanishHandler().setVanished(player, true);

        teleportStops = new ArrayList<>();

        for(Player teleportationCanidate : Bukkit.getServer().getOnlinePlayers()){
            if((!teleportationCanidate.hasPermission("goinroundplus.donotwatch")) && (!player.getName().equals(teleportationCanidate.getName()))){
                teleportStops.add(teleportationCanidate);
            }
        }
    }

    protected void doInterval(){
        if(player.isOnline()){
            Player observee = teleportStops.get(currentIndex);

            player.teleport(observee.getLocation());

            if(observee.isOnline()){
                player.sendMessage(goinRoundPlus.getConfigManager().getMessage("onteleportonline").replace("[player]", observee.getName()));
            }else{
                player.sendMessage(goinRoundPlus.getConfigManager().getMessage("onteleportoffline").replace("[player]", observee.getName()));
            }

            currentIndex++;
        }
    }

    protected void doEnd(){
        player.teleport(originalLocation);
        player.setAllowFlight(wasEnableFly);
        player.setFlying(wasFlying);
        goinRoundPlus.getVanishHandler().setVanished(player, wasVanished);

        player.sendMessage(goinRoundPlus.getConfigManager().getMessage("cyclefinished"));
    }

    /**
     * Returns whether or not this task is done
     * @return boolean
     */
    public boolean isDone(){
        return currentIndex >= teleportStops.size();
    }

    /**
     * Gets the player associataed with this TeleportTask
     * @return org.bukkit.entity.Player
     */
    public Player getPlayer(){
        return player;
    }

}
