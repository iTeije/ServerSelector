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

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

            try {
                Boolean isInQueue = queueManager.isInQueue(uuid);

                if (isInQueue) {
                    queueManager.quitQueue(uuid);
                    instance.getCommunicationModule().sendMessage(StorageKey.QUEUE_LEFT, player, sender);
                } else {
                    instance.getCommunicationModule().sendMessage(StorageKey.QUEUE_NOT_QUEUED, player, sender);
                }

            } catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace();
            }
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in QueuePlayerHandler.", exception);
        }
    }
}
