package eu.iteije.serverselector.bungee.messaging.handlers;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeCommunicationImplementation;
import eu.iteije.serverselector.bungee.queue.BungeeQueueManager;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class QueuePlayerHandler implements BungeeCommunicationImplementation {

    private ServerSelectorBungee instance;
    private BungeeQueueManager queueManager;

    public QueuePlayerHandler(ServerSelectorBungee instance) {
        this.instance = instance;
        this.queueManager = instance.getQueueManager();
    }

    @Override
    public void process(DataInputStream input, String sender) {
        try {
            UUID uuid = UUID.fromString(input.readUTF());
            String server = input.readUTF().toLowerCase();

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

            try {
                Boolean isInQueue = queueManager.isInQueue(uuid);

                ServerSelectorLogger.console("Queueing " + player.getName() + " for server " + server);

                if (isInQueue) {
                    String currentQueue = instance.getQueueManager().getCurrentQueue(uuid);
                    instance.getCommunicationModule().sendMessage(StorageKey.QUEUE_ALREADY_QUEUED, player, sender,
                            new Replacement("{server}", currentQueue)
                    );
                } else {
                    instance.getQueueManager().queuePlayer(server, uuid);
                    instance.getCommunicationModule().sendMessage(StorageKey.QUEUE_PROCESSING, player, sender,
                            new Replacement("{server}", server)
                    );
                }

            } catch (NullPointerException nullPointerException) {
                ServerSelectorLogger.console("Queueing " + player.getName() + " for server " + server);
                instance.getQueueManager().queuePlayer(server, uuid);
                instance.getCommunicationModule().sendMessage(StorageKey.QUEUE_PROCESSING, player, sender,
                        new Replacement("{server}", server)
                );
                nullPointerException.printStackTrace();
            }
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in QueuePlayerHandler.", exception);
        }

    }
}
