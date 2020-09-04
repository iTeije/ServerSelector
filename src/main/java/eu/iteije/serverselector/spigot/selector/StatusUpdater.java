package eu.iteije.serverselector.spigot.selector;

import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.metrics.SpigotMetricsModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

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

    @Getter private final Jedis jedis;
    @Getter private final Pipeline pipeline;

    private final SpigotMetricsModule metrics;

    public StatusUpdater(ServerSelectorSpigot serverSelectorSpigot) {
        this.instance = serverSelectorSpigot;
        this.metrics = instance.getSpigotMetricsModule();

        String redisHost = SpigotFileModule.getFile(StorageKey.CONFIG_REDIS_HOST).getString(StorageKey.CONFIG_REDIS_HOST);
        String redisPassword = SpigotFileModule.getFile(StorageKey.CONFIG_REDIS_PASSWORD).getString(StorageKey.CONFIG_REDIS_PASSWORD);
        Integer redisPort = SpigotFileModule.getFile(StorageKey.CONFIG_REDIS_PORT).getInt(StorageKey.CONFIG_REDIS_PORT);

        this.jedis = new Jedis(redisHost, redisPort, 5000);
        this.jedis.auth(redisPassword);
        this.pipeline = jedis.pipelined();

        this.updateDelay = SpigotFileModule.getFile(StorageKey.CONFIG_UPDATE_DELAY).getInt(StorageKey.CONFIG_UPDATE_DELAY);
        this.fetchDelay = SpigotFileModule.getFile(StorageKey.CONFIG_FETCH_DELAY).getInt(StorageKey.CONFIG_FETCH_DELAY);
    }

    public void updateServerInfo(Map<String, String> force) {
        try {
            if (this.jedis == null) {
                ServerSelectorLogger.console("Jedis is null. Couldn't update server info.");
                return;
            }

            if (this.jedis.isConnected()) {
                Map<String, String> serverData = new HashMap<>();

                // Status (whitelisted/online)
                if (instance.getServer().hasWhitelist() || force.getOrDefault("status", "").equalsIgnoreCase("whitelisted")) {
                    serverData.put("status", "WHITELISTED");
                } if (!instance.getServer().hasWhitelist() ||
                        force.getOrDefault("status", "").equalsIgnoreCase("online")){
                    serverData.put("status", "ONLINE");
                }

                // Current players
                serverData.put("players",  String.valueOf(instance.getServer().getOnlinePlayers().size()));

                // Max players
                serverData.put("max_players", String.valueOf(instance.getServer().getMaxPlayers()));

                // Current unix timestamp
                serverData.put("unix", String.valueOf((System.currentTimeMillis() / 1000L)));

                // Queue delay
                int queueDelay = SpigotFileModule.getFile(StorageKey.CONFIG_QUEUE_DELAY).getInt(StorageKey.CONFIG_QUEUE_DELAY);
                serverData.put("queue_delay", String.valueOf(queueDelay));

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
                serverData.put("whitelist", whitelist);

                // MOTD
                serverData.put("motd", instance.getServer().getMotd());

                // Version (string)
                serverData.put("version", instance.getServer().getBukkitVersion());

                // Tps (string)
                DecimalFormat format = new DecimalFormat("#.##");
                Double average = instance.getRunnableManager().getSelectorTimer().getAverageTPS();
                serverData.put("tps", format.format(average));

                // Uptime in minutes (long)
                serverData.put("uptime", String.valueOf(((System.currentTimeMillis() / 1000L) - instance.getStart()) / 60));

                // Current memory usage (long)
                serverData.put("current_memory", String.valueOf((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1048576L));

                // Max memory usage (long)
                serverData.put("max_memory", String.valueOf(Runtime.getRuntime().maxMemory() / 1048576L));

                pipeline.hmset("serverselector_" + instance.getServer().getPort(), serverData);
                pipeline.sync();

                metrics.redisCall();
            } else {
                ServerSelectorLogger.console("Couldn't update server data -> Not connected to Redis server.");
            }
        } catch (Exception exception) {
            ServerSelectorLogger.console("Couldn't update server data.", exception);
            exception.printStackTrace();
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
