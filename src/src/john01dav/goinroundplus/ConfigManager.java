package src.john01dav.goinroundplus;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

/**
 * This class manages the configuration by
 * filling it with defaults, writing those
 * defaults if needed, caching
 * configuration values to prevent
 * repeated access to the Bukkit API and
 * processing the values (ie. adding the
 * prefix to the messages)
 */
public class ConfigManager{
    private GoinRoundPlus goinRoundPlus;
    private FileConfiguration config;

    private double teleportDelay;
    private boolean vanishDuringCycle;
    private HashMap<String, String> messagesHashMap;

    protected ConfigManager(GoinRoundPlus goinRoundPlus){
        this.goinRoundPlus = goinRoundPlus;
    }

    protected void onInitialize(){
        String prefix;

        config = goinRoundPlus.getConfig();

        config.addDefault("general.teleportDelay", 7.0);
        config.addDefault("general.vanishDuringCycle", true);
        config.addDefault("general.prefix", "&4[&cGoinRound&4] ");

        config.addDefault("messages.onteleportonline", "&aYou have teleported to [player] and they are still online.");
        config.addDefault("messages.onteleportoffline", "&aYou have been teleported to [player]'s last known location. They have left since the GoinRoundPlus cycle started.");
        config.addDefault("messages.playersonly", "&cOnly players can perform that command.");
        config.addDefault("messages.cyclestarted", "&aYou have successfully started a cycle! It may take up to your configured interval time for the cycle to begin.");
        config.addDefault("messages.cyclefinished", "&aThe teleport cycle is finished and you have been teleported back to your original location.");
        config.addDefault("messages.vanished", "&aYou are not invisible to all other players except those who have been configured to be able to see you.");
        config.addDefault("messages.unvanished", "&aYou are no longer invisible to any other players.");
        config.addDefault("messages.pluginendstopcycle", "&cYour current cycle had to be stopped early because the server is reloading or restarting.");
        config.addDefault("messages.pluginendunvanished", "&cA reload or restart has forced you to become visible.");

        config.options().copyDefaults(true);
        goinRoundPlus.saveConfig();

        teleportDelay = config.getDouble("general.teleportDelay");
        vanishDuringCycle = config.getBoolean("general.vanishDuringCycle");

        messagesHashMap = new HashMap<>();
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("general.prefix"));

        for(String messageKey : config.getConfigurationSection("messages").getKeys(false)){
            messagesHashMap.put(messageKey, prefix + ChatColor.translateAlternateColorCodes('&', config.getString("messages." + messageKey)));
        }
    }

    /**
     * Gets the delay, in seconds, between teleports for tasks
     * @return double
     */
    public double getTeleportDelay(){
        return teleportDelay;
    }

    /**
     * Gets whether or not a player should be vanished during a cycle
     * @return boolean
     */
    public boolean isVanishDuringCycle(){
        return vanishDuringCycle;
    }

    /**
     * Gets the colored and prefixed message for the specified messageKey
     * @param messageKey String
     * @return String
     */
    public String getMessage(String messageKey){
        return messagesHashMap.get(messageKey);
    }

}
