package src.john01dav.goinroundplus;

import org.bukkit.plugin.java.JavaPlugin;

import src.john01dav.goinroundplus.commands.CommandStartCycle;
import src.john01dav.goinroundplus.commands.CommandVanish;
import src.john01dav.goinroundplus.teleport.TeleportDispatcher;
import src.john01dav.goinroundplus.vanish.VanishHandler;

/**
 * This class it the primary class of the
 * plugin with the purpose of coordinating
 * and initializing all other classes
 */
public class GoinRoundPlus extends JavaPlugin {
    private ConfigManager configManager;
    private TeleportDispatcher teleportDispatcher;
    private VanishHandler vanishHandler;

    @Override
    public void onEnable(){
        configManager = new ConfigManager(this);
        configManager.onInitialize();

        teleportDispatcher = new TeleportDispatcher(this);
        teleportDispatcher.onInitialize();

        vanishHandler = new VanishHandler(this);
        vanishHandler.onInitialize();

        //command classes handle registration automatically
        new CommandStartCycle(this);
        new CommandVanish(this);

        getLogger().info("GoinRoundPlus has been enabled!");
    }

    @Override
    public void onDisable(){
        teleportDispatcher.onDeinitialize();
        vanishHandler.onDeinitialize();

        getLogger().info("GoinRoundPlus has been disabled.");
    }

    /**
     * Gets the singleton responsible for managing this plugin's configuration file
     * @return src.john01dav.goinroundplus.ConfigManager
     */
    public ConfigManager getConfigManager(){
        return configManager;
    }

    /**
     * Gets the singleton responsible for dispatching and keeping track of currently running cycles
     * @return src.john01dav.teleport.TeleportDispatcher
     */
    public TeleportDispatcher getTeleportDispatcher(){
        return teleportDispatcher;
    }

    /**
     * Gets the singleton responsible for handling the vanishing of players
     * @return src.john01dav.vanish.VanishHandler
     */
    public VanishHandler getVanishHandler(){
        return vanishHandler;
    }

}
