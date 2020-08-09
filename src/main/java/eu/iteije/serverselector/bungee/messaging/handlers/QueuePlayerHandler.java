package eu.iteije.serverselector.bungee.messaging.handlers;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeHandlerImplementation;
import eu.iteije.serverselector.bungee.queue.BungeeQueueManager;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class QueuePlayerHandler implements BungeeHandlerImplementation {

    private final ServerSelectorBungee instance;
    private final BungeeQueueManager queueManager;

    public QueuePlayerHandler(ServerSelectorBungee instance) {
        this.instance = instance;
        this.queueManager = instance.getQueueManager();
    }

    @Override
    public void process(DataInputStream input, String sender) {
        try {
            String name = input.readUTF();
            String serverInput = input.readUTF().toLowerCase();

            // Split up the server input to all given servers
            List<String> servers = Arrays.asList(serverInput.split("\\|"));

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);

            try {
                Boolean isInQueue = queueManager.isInQueue(player.getUniqueId());

                if (isInQueue) {
                    // Get the first server in the list of the queues the player is currently in
                    String currentQueue = instance.getQueueManager().getCurrentQueue(player.getUniqueId()).get(0);
                    instance.getCommunicationModule().sendMessage(StorageKey.QUEUE_ALREADY_QUEUED, player, sender,
                            new Replacement("{server}", currentQueue)
                    );
                } else {
                    if (servers.contains(player.getServer().getInfo().getName().toLowerCase())) {
                        instance.getCommunicationModule().sendMessage(StorageKey.SEND_ALREADY_CONNECTED, player, sender,
                                new Replacement("{server}", player.getServer().getInfo().getName().toLowerCase())
                        );
                        return;
                    }

                    queuePlayer(servers, player, sender);

                }

            } catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace();
            }
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in QueuePlayerHandler.", exception);
        }

    }

    public void queuePlayer(List<String> servers, ProxiedPlayer player, String sender) {
        UUID uuid = player.getUniqueId();

        for (String server : servers) {
            instance.getQueueManager().queuePlayer(server, uuid);

            ServerSelectorLogger.console("Queueing " + player.getName() + " for server " + server);

            instance.getCommunicationModule().sendMessage(StorageKey.QUEUE_PROCESSING, player, sender,
                    new Replacement("{server}", server)
            );
        }
    }
}
