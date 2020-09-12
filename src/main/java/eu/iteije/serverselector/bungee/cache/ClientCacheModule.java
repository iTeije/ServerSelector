package eu.iteije.serverselector.bungee.cache;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.metrics.BungeeMetrics;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ClientCacheModule {

    @Getter private final Map<String, ServerData> serverData;

    private final ServerSelectorBungee instance;
    private final BungeeMetrics metrics;

    public ClientCacheModule(ServerSelectorBungee instance, BungeeMetrics metrics) {
        this.instance = instance;

        this.serverData = new HashMap<>();
        this.metrics = metrics;
    }

    /**
     * @param data received server information (sockets)
     */
    public void updateServerData(ServerData data) {
        metrics.spigotRedisCall(data.getRedisCalls());

        // Get the previous ServerData entry for the specific server
        ServerData previous = serverData.get(data.getServerName());

        // Update queue processing based on received status
        if (data.getStatus().equalsIgnoreCase("ONLINE")) {
            // Check whether the previous serverdata actually exists
            if (previous != null) {
                // Start processing the queue if it isn't already
                if (!previous.getStatus().equalsIgnoreCase(data.getStatus())) {
                    // Start processing the players in queue
                    instance.getQueueManager().processQueue(data.getServerName(), data.getQueueDelay());
                }
            } else {
                // Start sending people
                instance.getQueueManager().processQueue(data.getServerName(), data.getQueueDelay());
            }
        }

        // Pause the queue if the server is whitelisted
        if (data.getStatus().equalsIgnoreCase("WHITELISTED")) {
            // Check whether the previous serverdata exists
            if (previous != null) {
                // If the previous status is not equal to 'whitelisted', the queue should be
                // paused and a whitelist queue should start. If it is, it shouldn't cancel the whitelist queue
                if (!previous.getStatus().equalsIgnoreCase("WHITELISTED")) {
                    instance.getQueueManager().pauseQueue(data.getServerName(), data.getQueueDelay());
                }
            } else {
                instance.getQueueManager().pauseQueue(data.getServerName(), data.getQueueDelay());
            }

        }

        // Replace the existing ServerData entry with the new one
        serverData.remove(data.getServerName());
        serverData.put(data.getServerName(), data);
    }

    /**
     * @param server server name (as stated in the config.yml file of the bungee server)
     * @return the ServerData entry
     */
    public ServerData getServerData(String server) {
        return serverData.getOrDefault(server, null);
    }
}
