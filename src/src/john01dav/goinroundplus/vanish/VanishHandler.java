package src.john01dav.goinroundplus.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import src.john01dav.goinroundplus.GoinRoundPlus;

import java.util.HashMap;
import java.util.UUID;

/**
 * This class keeps track of who is vanished
 * and who isn't along with handling the
 * appropriate show() and hide() method
 * calls on the needed players
 */
public class VanishHandler implements Listener{
    private GoinRoundPlus goinRoundPlus;
    private HashMap<UUID, Boolean> vanishedHashMap;

    public VanishHandler(GoinRoundPlus goinRoundPlus){
        this.goinRoundPlus = goinRoundPlus;
    }

    public void onInitialize(){
        vanishedHashMap = new HashMap<>();
        goinRoundPlus.getServer().getPluginManager().registerEvents(this, goinRoundPlus);

        //using two loops to ensure that all player's have a boolean set in the vanishedHashMap before any calls are done
        //using loops at all because /reload
        for(Player player : goinRoundPlus.getServer().getOnlinePlayers()){
            vanishedHashMap.put(player.getUniqueId(), false);
        }

        for(Player player : goinRoundPlus.getServer().getOnlinePlayers()){
            refreshVanishFor(player);
        }
    }

    public void onDeinitialize(){
        String pluginEndUnvanished = goinRoundPlus.getConfigManager().getMessage("pluginendunvanished");

        for(Player player : goinRoundPlus.getServer().getOnlinePlayers()){
            if(getVanished(player)){
                player.sendMessage(pluginEndUnvanished);
            }
        }
    }

    /**
     * Gets whether or not the specified player is vanished
     * @param player org.bukkit.entity.Player
     * @return boolean
     */
    public boolean getVanished(Player player){
        return vanishedHashMap.get(player.getUniqueId());
    }

    /**
     * Sets whether or not the specified player is vanished
     * @param player org.bukkit.entity.Player
     * @param vanished boolean
     */
    public void setVanished(Player player, boolean vanished){
        vanishedHashMap.put(player.getUniqueId(), vanished);
        refreshVanishFor(player);
    }

    @SuppressWarnings("unused")
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void playerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        vanishedHashMap.put(player.getUniqueId(), false);

        for(Player currentOtherPlayer : goinRoundPlus.getServer().getOnlinePlayers()){
            if(!currentOtherPlayer.getName().equals(player.getName())){
                refreshVanish(player, currentOtherPlayer);
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void playerQuitEvent(PlayerQuitEvent e){
        vanishedHashMap.remove(e.getPlayer().getUniqueId());
    }

    @SuppressWarnings("unused")
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void entityTargetEvent(EntityTargetEvent e){
        if(e.getTarget() instanceof Player){
            Player player = ((Player) e.getTarget());
            if(getVanished(player)){
                e.setCancelled(true);
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void playerPickupItemEvent(PlayerPickupItemEvent e){
        if(getVanished(e.getPlayer())){
            e.setCancelled(true);
        }
    }

    /**
     * Refreshes whether or not the specified player is visible to other players
     * This method is intended to be called after the vanish state of a player changes
     * @param player org.bukkit.entity.Playaer
     */
    private void refreshVanishFor(Player player){
        for(Player currentOtherPlayer : goinRoundPlus.getServer().getOnlinePlayers()){
            if(!currentOtherPlayer.getName().equals(player.getName())){
                refreshVanish(currentOtherPlayer, player);
            }
        }
    }

    /**
     * Refreshes the individual vanish state between two players
     * ie. if other was recently vanished, this method makes other invisible to player
     * @param player The player who's refreshed
     * @param other Who is refreshed to the player
     */
    private void refreshVanish(Player player, Player other){
        if(!player.hasPermission("goinroundplus.vanishbypass")){
            if(getVanished(other)){
                player.hidePlayer(other);
            }else{
                player.showPlayer(other);
            }
        }
    }

}
