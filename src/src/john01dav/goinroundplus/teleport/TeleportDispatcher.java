package src.john01dav.goinroundplus.teleport;

import org.bukkit.scheduler.BukkitRunnable;

import src.john01dav.goinroundplus.GoinRoundPlus;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class handles the currently running TeleportTasks.
 * It is used instead of a separate scheduler task for
 * every cycle because the Bukkit scheduler, in the need
 * to be thread safe, does not handle large numbers of
 * tasks well
 */
public class TeleportDispatcher {
    private GoinRoundPlus goinRoundPlus;
    private ArrayList<TeleportTask> teleportTasks;

    public TeleportDispatcher(GoinRoundPlus goinRoundPlus){
        this.goinRoundPlus = goinRoundPlus;
    }

    public void onInitialize(){
        teleportTasks = new ArrayList<>();

        new TeleportDispatcherBukkitRunnable().runTaskTimer(goinRoundPlus, 0, Math.round(goinRoundPlus.getConfigManager().getTeleportDelay() * 20));
    }

    public void onDeinitialize(){
        String pluginEndStopCycle = goinRoundPlus.getConfigManager().getMessage("pluginendstopcycle");

        for(TeleportTask teleportTask : teleportTasks){
            teleportTask.doEnd();
            teleportTask.getPlayer().sendMessage(pluginEndStopCycle);
        }
        teleportTasks.clear(); //although the doInterval() method probably won't be run again, we are clearing it just for good measure (mainly to prevent multiple calls to doEnd())
    }

    /**
     * Adds a TeleportTask to the current list of running tasks
     * @param teleportTask TeleportTask
     */
    public void addTeleportTask(TeleportTask teleportTask){
        teleportTasks.add(teleportTask);
    }

    private void doInterval(){
        Iterator<TeleportTask> taskIterator = teleportTasks.iterator();

        while(taskIterator.hasNext()){
            TeleportTask teleportTask = taskIterator.next();
            if(teleportTask.isDone()){
                teleportTask.doEnd();
                taskIterator.remove();
            }else{
                teleportTask.doInterval();
            }
        }
    }

    private class TeleportDispatcherBukkitRunnable extends BukkitRunnable{
        public void run(){
            doInterval();
        }
    }

}
