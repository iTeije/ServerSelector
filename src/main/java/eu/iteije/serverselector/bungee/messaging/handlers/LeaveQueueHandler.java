package eu.iteije.serverselector.bungee.messaging.handlers;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeHandlerImplementation;
import eu.iteije.serverselector.bungee.queue.BungeeQueueManager;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class LeaveQueueHandler implements BungeeHandlerImplementation {

    private ServerSelectorBungee instance;
    private BungeeQueueManager queueManager;

    public LeaveQueueHandler(ServerSelectorBungee instance) {
        this.instance = instance;
        this.queueManager = instance.getQueueManager();
    }

    @Override
    public void process(DataInputStream input, String sender) {
        try {
            UUID uuid = UUID.fromString(input.readUTF());
            ServerSelectorLogger.console("UUID: " + uuid);

            try {
                Boolean isInQueue = queueManager.isInQueue(uuid);
                ServerSelectorLogger.console("in queue: " + isInQueue);

                if (isInQueue) {
                    queueManager.quitQueue(uuid);
                    sendMessage(StorageKey.QUEUE_LEFT, uuid, sender);
                } else {
                    sendMessage(StorageKey.QUEUE_NOT_QUEUED, uuid, sender);
                }

            } catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace();
            }
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in QueuePlayerHandler.", exception);
        }
    }

    public void sendMessage(StorageKey key, UUID uuid, String sender) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (uuid != null || sender == null) {
            instance.getCommunicationModule().sendMessage(key, player, sender);
        }
    }
}
