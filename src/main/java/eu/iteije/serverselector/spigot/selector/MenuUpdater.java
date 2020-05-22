package eu.iteije.serverselector.spigot.selector;

import eu.iteije.serverselector.common.networking.objects.ServerData;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import lombok.Getter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuUpdater {

    private ServerSelectorSpigot instance;
    // Delay in seconds
    @Getter private int delay;

    private List<Integer> tasks = new ArrayList<>();

    private HashMap<String, ServerData> serverData = new HashMap<>();

    private Socket socket;

    public MenuUpdater(ServerSelectorSpigot serverSelectorSpigot) {
        this.instance = serverSelectorSpigot;

        this.delay = SpigotFileModule.getFile(StorageKey.SELECTOR_UPDATE_DELAY).getInt(StorageKey.SELECTOR_UPDATE_DELAY);
    }

    public void initializeSocket() {
        try {
            this.socket = new Socket("127.0.0.1", instance.getServer().getPort() + 10000);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void updateServerInfo() {
        try {
            if (socket == null) initializeSocket();

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            dataOutputStream.writeUTF("serverinfo");
            dataOutputStream.writeUTF(String.valueOf(instance.getServer().getPort()));

            // Status
            if (instance.getServer().hasWhitelist()) {
                dataOutputStream.writeUTF("WHITELISTED");
            } else {
                dataOutputStream.writeUTF("ONLINE");
            }
            // Current players
            dataOutputStream.writeUTF(String.valueOf(instance.getServer().getOnlinePlayers().size()));
            // Max players
            dataOutputStream.writeUTF(String.valueOf(instance.getServer().getMaxPlayers()));
            dataOutputStream.flush();

            dataOutputStream.close();
            socket.close();
            socket = null;

            instance.getServer().getScheduler().scheduleAsyncDelayedTask(instance, this::initializeSocket, 10L);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public void initializeUpdateScheduler() {
        tasks.add(instance.getServer().getScheduler().scheduleSyncRepeatingTask(this.instance, () -> {
            updateServerInfo();
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

    public void updateServerInfo(ServerData info) {
        if (serverData.containsKey(info.getServerName())) serverData.remove(info.getServerName());
        serverData.put(info.getServerName(), info);
    }

    public ServerData getServerInfo(String server) {
        if (serverData.containsKey(server)) return serverData.get(server);
        return null;
    }

}
