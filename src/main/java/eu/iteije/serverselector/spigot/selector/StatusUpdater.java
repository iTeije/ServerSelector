package eu.iteije.serverselector.spigot.selector;

import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusUpdater {

    private final ServerSelectorSpigot instance;

    // Update delay in seconds
    @Getter
    private final int updateDelay;
    // Fetch delay in seconds
    @Getter
    private final int fetchDelay;

    private final List<Integer> tasks = new ArrayList<>();

    private final HashMap<String, ServerData> serverData = new HashMap<>();

    @Getter private Socket socket;

    public StatusUpdater(ServerSelectorSpigot serverSelectorSpigot) {
        this.instance = serverSelectorSpigot;

        initializeSocket();

        this.updateDelay = SpigotFileModule.getFile(StorageKey.CONFIG_UPDATE_DELAY).getInt(StorageKey.CONFIG_UPDATE_DELAY);
        this.fetchDelay = SpigotFileModule.getFile(StorageKey.CONFIG_FETCH_DELAY).getInt(StorageKey.CONFIG_FETCH_DELAY);
    }

    public void initializeSocket() {
        try {
            this.socket = new Socket(SpigotFileModule.getFile(StorageKey.CONFIG_BUNGEE_IP).getString(StorageKey.CONFIG_BUNGEE_IP),
                    instance.getServer().getPort() + 10000);
        } catch (IOException exception) {
            ServerSelectorLogger.console("Failed to initialize socket on " + (instance.getServer().getPort() + 10000) + ".", exception);
        }
    }

    public void updateServerInfo(Map<String, String> force) {
        try {
            if (socket == null) {
                initializeSocket();
                return;
            }

            if (this.socket.isConnected()) {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                dataOutputStream.writeUTF("serverinfo");
                dataOutputStream.writeUTF(String.valueOf(instance.getServer().getPort()));

                // Status
                if (instance.getServer().hasWhitelist() ||
                        force.getOrDefault("status", "").equalsIgnoreCase("whitelisted")) {
                    dataOutputStream.writeUTF("WHITELISTED");
                } else if (!instance.getServer().hasWhitelist() ||
                        force.getOrDefault("status", "").equalsIgnoreCase("online")){
                    dataOutputStream.writeUTF("ONLINE");
                }

                // Current players
                dataOutputStream.writeUTF(String.valueOf(instance.getServer().getOnlinePlayers().size()));
                // Max players
                dataOutputStream.writeUTF(String.valueOf(instance.getServer().getMaxPlayers()));


                // Current unix timestamp
                long unix = System.currentTimeMillis() / 1000L;
                dataOutputStream.writeLong(unix);


                // Queue delay
                int queueDelay = SpigotFileModule.getFile(StorageKey.CONFIG_QUEUE_DELAY).getInt(StorageKey.CONFIG_QUEUE_DELAY);
                dataOutputStream.writeInt(queueDelay);

                // Whitelisted players
                List<String> whitelistedPlayers = new ArrayList<>();

                for (OfflinePlayer offlinePlayer : instance.getServer().getWhitelistedPlayers()) {
                    whitelistedPlayers.add(offlinePlayer.getUniqueId().toString());
                }


                // Convert list to string and replace brackets and spaces
                String whitelist = whitelistedPlayers.toString();
                whitelist = whitelist.replaceAll("\\s", "");
                whitelist = whitelist.replaceAll("[\\[\\](){}]", "");

                // Write whitelisted players
                dataOutputStream.writeUTF(whitelist);

                // Motd (string)
                dataOutputStream.writeUTF(instance.getServer().getMotd());

                // Version (string)
                dataOutputStream.writeUTF(instance.getServer().getBukkitVersion());

                // Tps (string)
                DecimalFormat format = new DecimalFormat("#.##");
                Double average = instance.getRunnableManager().getSelectorTimer().getAverageTPS();
                dataOutputStream.writeUTF(format.format(average));

                // Uptime in minutes (long)
                dataOutputStream.writeLong(((System.currentTimeMillis() / 1000L) - instance.getStart()) / 60);

                // Current memory usage (long)
                dataOutputStream.writeLong((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1048576L);
                // Max memory usage (long)
                dataOutputStream.writeLong(Runtime.getRuntime().maxMemory() / 1048576L);

                dataOutputStream.flush();

                socket.close();
                socket = null;
            } else {
                initializeSocket();
            }
        } catch (IOException exception) {
            ServerSelectorLogger.console("Couldn't update server data.", exception);
            if (this.socket != null) {
                try {
                    this.socket.close();
                    this.socket = null;
                } catch (IOException ioException) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public void initializeUpdateScheduler() {
        tasks.add(instance.getServer().getScheduler().scheduleSyncRepeatingTask(this.instance, () -> {
            updateServerInfo(new HashMap<>());
        }, 0L, this.updateDelay * 20L));
    }

    public void initializeFetchScheduler() {
        tasks.add(instance.getServer().getScheduler().scheduleSyncRepeatingTask(this.instance, () -> {
            // Only fetch if there is at least 1 player online
            if (Bukkit.getOnlinePlayers().size() != 0) {
                instance.getMenuModule().deleteCachedMenus();
                instance.getSelectorModule().cacheMenus();
            }
        }, 0L, this.fetchDelay * 20L));
    }

    public void destroyTasks() {
        for (Integer task : this.tasks) {
            instance.getServer().getScheduler().cancelTask(task);
        }
    }

    public void updateServerInfo(ServerData info) {
        serverData.remove(info.getServerName());
        serverData.put(info.getServerName(), info);
    }

    public ServerData getServerInfo(String server) {
        if (serverData.containsKey(server)) return serverData.get(server);
        return null;
    }

    public void removeServerInfo(String server) {
        serverData.remove(server);
    }

}
