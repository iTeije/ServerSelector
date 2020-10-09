package eu.iteije.serverselector.bungee.networking;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.files.BungeeFileModule;
import eu.iteije.serverselector.bungee.metrics.BungeeMetrics;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;
import redis.clients.jedis.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BungeeRedisManager {

    private final ServerSelectorBungee instance;
    private final BungeeMetrics metrics;

    @Getter private final JedisPool jedisPool;

    public BungeeRedisManager(ServerSelectorBungee instance) {
        this.instance = instance;
        this.metrics = instance.getMetricsModule();

        String redisHost = BungeeFileModule.getFile(StorageKey.CONFIG_REDIS_HOST).getString(StorageKey.CONFIG_REDIS_HOST);
        String redisPassword = BungeeFileModule.getFile(StorageKey.CONFIG_REDIS_PASSWORD).getString(StorageKey.CONFIG_REDIS_PASSWORD);
        Integer redisPort = BungeeFileModule.getFile(StorageKey.CONFIG_REDIS_PORT).getInt(StorageKey.CONFIG_REDIS_PORT);

        this.jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort, Protocol.DEFAULT_TIMEOUT, redisPassword);

        initializeSchedulers();
    }

    public void initializeSchedulers() {
        // Loop through all servers to open up a socket for every single server
        if (jedisPool != null && jedisPool.getResource().isConnected()) {
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
            try (Jedis jedis = this.jedisPool.getResource()) {
                Pipeline pipeline = jedis.pipelined();
                Response<Map<String, String>> response = pipeline.hgetAll("serverselector_" + port);
                pipeline.sync();
                metrics.redisCall();
                if (response != null) {
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
                                    Long.parseLong(data.get("max_memory")),
                                    Integer.parseInt(data.get("redis_calls"))
                            );

                            instance.getClientCacheModule().updateServerData(serverData);
                        } catch (NumberFormatException exception) {
                            ServerSelectorLogger.console("Parsing failed (" + port + "). " +
                                    "Unix: " + data.get("unix") +
                                    ", Queue delay: " + data.get("queue_delay") +
                                    ", Uptime: " + data.get("uptime") +
                                    ", Current memory: " + data.get("current_memory") +
                                    ", Max memory: " + data.get("max_memory") +
                                    ", Redis calls: " + data.get("redis_calls")
                            );
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        ServerSelectorLogger.console("Couldn't fetch data for port " + port);
                    }
                } else {
                    ServerSelectorLogger.console("Response is null");
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
