package eu.iteije.serverselector.bungee.networking;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.files.BungeeFileModule;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BungeeRedisManager {

    private final ServerSelectorBungee instance;

    @Getter
    private final Jedis jedis;
    @Getter
    private final Pipeline pipeline;

    public BungeeRedisManager(ServerSelectorBungee instance) {
        this.instance = instance;

        String redisHost = BungeeFileModule.getFile(StorageKey.CONFIG_REDIS_HOST).getString(StorageKey.CONFIG_REDIS_HOST);
        String redisPassword = BungeeFileModule.getFile(StorageKey.CONFIG_REDIS_PASSWORD).getString(StorageKey.CONFIG_REDIS_PASSWORD);

        this.jedis = new Jedis(redisHost, 6379, 5000);
        this.jedis.auth(redisPassword);

        System.out.println(jedis.isConnected() + " - " + redisHost + " - " + redisPassword);

        this.pipeline = jedis.pipelined();
    }

    public void initializeSchedulers() {
        // Loop through all servers to open up a socket for every single server
        if (jedis != null && jedis.isConnected()) {
            Map<String, ServerInfo> servers = instance.getProxy().getServers();
            for (ServerInfo server : servers.values()) {
                int port = server.getAddress().getPort();
                new Thread(() -> {
                    instance.getLogger().info("Starting Redis scheduler for server on port " + port);
                    initializeScheduler(port, server.getName());
                }).start();
            }
        } else {
            ServerSelectorLogger.console("Couldn't initialize Redis schedulers, Jedis is null or is not connected.");
        }
    }

    public void initializeScheduler(int port, String serverName) {
        instance.getProxy().getScheduler().schedule(instance, () -> {
            Response<Map<String, String>> response = pipeline.hgetAll("serverselector_" + port);
            pipeline.sync();
            Map<String, String> data = response.get();

            if (data != null) {
                try {
                    ServerData serverData = new ServerData(
                            serverName,
                            data.get("status"),
                            data.get("players"),
                            data.get("max_players"),
                            Long.parseLong(data.get("unix")),
                            instance.getQueueManager().getQueueSize(serverName),
                            Integer.parseInt(data.get("queue_delay")),
                            data.get("whitelist").split(","),
                            data.get("motd"),
                            data.get("version"),
                            data.get("tps"),
                            Long.parseLong(data.get("uptime")),
                            Long.parseLong(data.get("current_memory")),
                            Long.parseLong(data.get("max_memory"))
                    );

                    instance.getClientCacheModule().updateServerData(serverData);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else {
                ServerSelectorLogger.console("Couldn't fetch data for port " + port);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
