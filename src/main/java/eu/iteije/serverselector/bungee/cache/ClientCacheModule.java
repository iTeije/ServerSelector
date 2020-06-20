package eu.iteije.serverselector.bungee.cache;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import lombok.Getter;

import java.util.HashMap;

public class ClientCacheModule {

    @Getter private HashMap<String, ServerData> serverData = new HashMap<>();

    private ServerSelectorBungee instance;

    public ClientCacheModule(ServerSelectorBungee instance) {
        this.instance = instance;
    }

    /**
     * @param data received server information (sockets)
     */
    public void updateServerData(ServerData data) {
        // Update queue processing based on received status
        if (data.getStatus().equalsIgnoreCase("ONLINE")) {
            // Get the previous ServerData entry for the specific server
            ServerData previous = serverData.get(data.getServerName());
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
            instance.getQueueManager().pauseQueue(data.getServerName());
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
