package eu.iteije.serverselector.bungee.networking;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BungeeRedisManager {

    private final ServerSelectorBungee instance;

    @Getter private final Jedis jedis;
    @Getter private final Pipeline pipeline;

    public BungeeRedisManager(ServerSelectorBungee instance) {
        this.instance = instance;

        String host = "127.0.0.1";

        // set up redis and its password, yes

        this.jedis = new Jedis(host, 6379, 5000);

        for (int i = 0; i < 10; i++) {
            System.out.println(jedis.isConnected() + " - " + host);
        }

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
                    initializeScheduler(port);
                }).start();
            }
        } else {
            ServerSelectorLogger.console("Couldn't initialize Redis schedulers, Jedis is null or is not connected.");
        }
    }

    public void initializeScheduler(int port) {
        instance.getProxy().getScheduler().schedule(instance, () -> {
            Map<String, String> data = pipeline.hgetAll("serverselector_" + port).get();
            if (data != null) {
                for (String key : data.keySet()) {
                    ServerSelectorLogger.console(key + " : " + data.get(key));
                }
            } else {
                ServerSelectorLogger.console("Couldn't fetch data for port " + port);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
