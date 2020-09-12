package eu.iteije.serverselector.bungee.queue;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BungeeQueueManager {

    private final ServerSelectorBungee instance;

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
        return queue.values().stream().anyMatch(queueList -> queueList.contains(uuid));
    }

    public List<String> getCurrentQueue(UUID uuid) {
        List<String> queues = new ArrayList<>();
        for (Map.Entry<String, LinkedList<UUID>> list : queue.entrySet()) {
            if (list.getValue().contains(uuid)) queues.add(list.getKey());
        }
        return queues;
    }

    public int getQueueSize(String server) {
        return getQueue(server).size();
    }

    public void quitQueue(UUID uuid) {
        List<String> currentQueue = getCurrentQueue(uuid);
        if (currentQueue != null) {
            for (String queue : currentQueue) {
                LinkedList<UUID> list = this.queue.get(queue);
                list.remove(uuid);

                updateQueue(queue, list);
            }
        }
    }

    private Map<String, ScheduledTask> queueTasks = new HashMap<>();

    public void processQueue(String server, int delay) {
        if (hasQueueTask(server)) getScheduledTask(server).cancel();

        LinkedList<UUID> currentQueue = getQueue(server);
        queueTasks.put(server, instance.getProxy().getScheduler().schedule(instance, () -> {
            if (currentQueue != null && currentQueue.size() > 0) {
                UUID uuid = currentQueue.get(0);

                instance.getCommunicationModule().sendPlayer(uuid, server);
                quitQueue(uuid);
                currentQueue.remove(uuid);
            }
        }, delay, delay, TimeUnit.MILLISECONDS));
    }

    public void pauseQueue(String server, int delay) {
        if (hasQueueTask(server)) {
            getScheduledTask(server).cancel();
        }

        /*
         * why is it not working? well the server data is usually updated once every second,
         * but it doesn't quite work if the runnable is cancelled every second, while it takes
         * 1.5 seconds by default to process the first person in the queue
         */
        if (instance.getClientCacheModule().getServerData(server) != null) {
            queueTasks.put(server, instance.getProxy().getScheduler().schedule(instance, () -> {
                String[] whitelist = instance.getClientCacheModule().getServerData(server).getWhitelist();
                LinkedList<UUID> currentQueue = getQueue(server);

                if (currentQueue.size() > 0) {
                    for (String uuid : whitelist) {
                        if (uuid == null || uuid.equals("")) break;
                        UUID validUUID = UUID.fromString(uuid);
                        if (currentQueue.contains(validUUID)) {
                            instance.getCommunicationModule().sendPlayer(validUUID, server);
                            quitQueue(validUUID);
                            currentQueue.remove(validUUID);
                            break;
                        }
                    }
                }
            }, delay, delay, TimeUnit.MILLISECONDS));

        }

    }

    public boolean hasQueueTask(String server) {
        return queueTasks.containsKey(server);
    }

    public ScheduledTask getScheduledTask(String server) {
        return queueTasks.get(server);
    }

    public void removePlayerFromQueues(String uuid) {

    }


}
