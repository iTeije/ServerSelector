package eu.iteije.serverselector.bungee.queue;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BungeeQueueManager {

    private ServerSelectorBungee instance;

    private HashMap<String, List<UUID>> queue;

    public BungeeQueueManager(ServerSelectorBungee instance) {
        this.instance = instance;

        queue = new HashMap<>();
        initializeQueue();
    }

    public void initializeQueue() {
        ProxyServer.getInstance().getServers().forEach((string, info) -> {
            queue.put(info.getName().toLowerCase(), new ArrayList<>());
        });
    }

    public void queuePlayer(String server, UUID uuid) {
        if (hasQueue(server)) {
            List<UUID> updated = getQueue(server);
            updated.add(uuid);

            updateQueue(server, updated);
        } else {
            List<UUID> init = new ArrayList<>();
            init.add(uuid);

            updateQueue(server, init);
        }
    }

    public Boolean hasQueue(String serverName) {
        return queue.containsKey(serverName);
    }

    public List<UUID> getQueue(String serverName) {
        return queue.get(serverName);
    }

    public void updateQueue(String serverName, List<UUID> uuids) {
        queue.put(serverName, uuids);
    }

    public Boolean isInQueue(UUID uuid) {
        if (queue.values().stream().anyMatch(queueList -> queueList.contains(uuid))) {
            return true;
        }
        return false;
    }

}
