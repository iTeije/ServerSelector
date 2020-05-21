package eu.iteije.serverselector.spigot.selector;

import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.selector.objects.ServerInfo;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuUpdater {

    private ServerSelectorSpigot instance;
    // Delay in seconds
    @Getter private int delay;

    private List<Integer> tasks = new ArrayList<>();

    private HashMap<String, ServerInfo> serverData = new HashMap<>();

    public MenuUpdater(ServerSelectorSpigot serverSelectorSpigot) {
        this.instance = serverSelectorSpigot;

        this.delay = SpigotFileModule.getFile(StorageKey.SELECTOR_UPDATE_DELAY).getInt(StorageKey.SELECTOR_UPDATE_DELAY);
    }

    public void initializeUpdateScheduler() {
        tasks.add(instance.getServer().getScheduler().scheduleSyncRepeatingTask(this.instance, () -> {
            instance.getMenuModule().deleteCachedMenus();
            instance.getSelectorModule().cacheMenus();
        }, 0L, delay * 20L));
    }

    public void destroyTasks() {
        for (Integer task : this.tasks) {
            instance.getServer().getScheduler().cancelTask(task);
        }
    }

    public void updateDelay(int seconds) {
        this.delay = seconds;

        destroyTasks();
        initializeUpdateScheduler();
    }

    public void updateServerInfo(ServerInfo info) {
        if (serverData.containsKey(info.getServerName())) serverData.remove(info.getServerName());
        Bukkit.broadcastMessage("MenuUpdated: storing information");
        serverData.put(info.getServerName(), info);
    }

    public ServerInfo getServerInfo(String server) {
        if (serverData.containsKey(server)) return serverData.get(server);
        return null;
    }

}
