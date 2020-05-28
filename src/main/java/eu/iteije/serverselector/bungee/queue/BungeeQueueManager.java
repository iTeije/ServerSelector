package eu.iteije.serverselector.bungee.queue;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BungeeQueueManager {

    private ServerSelectorBungee instance;

    private HashMap<String, List<UUID>> queue;

    public BungeeQueueManager(ServerSelectorBungee instance) {
        this.instance = instance;

        queue = new HashMap<>();
    }

    public void queuePlayer(String server, UUID uuid) {
        if (hasQueue(server)) {
            List<UUID> updated = getQueue(server);
            updated.add(uuid);

            updateQueue(server, updated);
        }
    }

    public boolean hasQueue(String serverName) {
        return queue.get(serverName) != null;
    }

    public List<UUID> getQueue(String serverName) {
        return queue.get(serverName);
    }

    public void updateQueue(String serverName, List<UUID> uuids) {
        queue.put(serverName, uuids);
    }


}
