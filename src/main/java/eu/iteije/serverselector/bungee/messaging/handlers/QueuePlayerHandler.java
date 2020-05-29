package eu.iteije.serverselector.bungee.messaging.handlers;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeCommunicationImplementation;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class QueuePlayerHandler implements BungeeCommunicationImplementation {

    ServerSelectorBungee instance;

    public QueuePlayerHandler(ServerSelectorBungee instance) {
        this.instance = instance;
    }

    @Override
    public void process(DataInputStream input, String sender) {
        try {
            UUID uuid = UUID.fromString(input.readUTF());
            instance.getQueueManager().queuePlayer(sender, uuid);
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in QueuePlayerHandler.", exception);
        }

    }
}
