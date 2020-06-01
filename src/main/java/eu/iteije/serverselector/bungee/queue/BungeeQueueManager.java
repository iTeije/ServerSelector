package eu.iteije.serverselector.bungee.queue;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BungeeQueueManager {

    private ServerSelectorBungee instance;

    private HashMap<String, LinkedList<UUID>> queue;

    public BungeeQueueManager(ServerSelectorBungee instance) {
        this.instance = instance;

        queue = new HashMap<>();
        initializeQueue();
    }

    public void initializeQueue() {
        ProxyServer.getInstance().getServers().forEach((string, info) -> {
            queue.put(info.getName().toLowerCase(), new LinkedList<>());
        });
    }

    public void queuePlayer(String server, UUID uuid) {
        if (hasQueue(server)) {
            LinkedList<UUID> updated = getQueue(server);
            updated.add(uuid);

            updateQueue(server, updated);
        } else {
            LinkedList<UUID> init = new LinkedList<>();
            init.add(uuid);

            updateQueue(server, init);
        }
    }

    public Boolean hasQueue(String serverName) {
        return queue.containsKey(serverName);
    }

    public LinkedList<UUID> getQueue(String serverName) {
        if (queue.get(serverName) != null) return queue.get(serverName);

        updateQueue(serverName, new LinkedList<>());
        return queue.get(serverName);
    }

    public void updateQueue(String serverName, LinkedList<UUID> uuids) {
        queue.put(serverName, uuids);
    }

    public Boolean isInQueue(UUID uuid) {
        if (queue.values().stream().anyMatch(queueList -> queueList.contains(uuid))) {
            return true;
        }
        return false;
    }

    public String getCurrentQueue(UUID uuid) {
        for (Map.Entry<String, LinkedList<UUID>> list : queue.entrySet()) {
            if (list.getValue().contains(uuid)) return list.getKey();
        }
        return null;
    }

    public int getQueueSize(String server) {
        return getQueue(server).size();
    }

    public void quitQueue(UUID uuid) {
        String currentQueue = getCurrentQueue(uuid);
        if (currentQueue != null) {
            LinkedList<UUID> list = queue.get(currentQueue);
            list.remove(uuid);

            updateQueue(currentQueue, list);
        }
    }

    private Map<String, ScheduledTask> queueTasks = new HashMap<>();

    public void processQueue(String server, int delay) {
        LinkedList<UUID> currentQueue = getQueue(server);
        queueTasks.put(server, instance.getProxy().getScheduler().schedule(instance, () -> {
            if (currentQueue.size() > 0) {
                instance.getCommunicationModule().sendPlayer(currentQueue.get(0), server);
                currentQueue.remove(0);
            }
        }, delay, delay, TimeUnit.MILLISECONDS));
    }

    public void pauseQueue(String server) {
        if (hasQueueTask(server)) {
            getScheduledTask(server).cancel();
        }
    }

    public boolean hasQueueTask(String server) {
        return queueTasks.containsKey(server);
    }

    public ScheduledTask getScheduledTask(String server) {
        return queueTasks.get(server);
    }


}
