package eu.iteije.serverselector.bungee.messaging.handlers;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.messaging.BungeeCommunicationModule;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeCommunicationImplementation;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.MessageModule;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.DataInputStream;
import java.io.IOException;

public class SendPlayerHandler implements BungeeCommunicationImplementation {

    private ServerSelectorBungee instance;

    public SendPlayerHandler(ServerSelectorBungee instance) {
        this.instance = instance;
    }

    @Override
    public void process(DataInputStream input, String sender) {
        try {
            // Reading input
            String server = input.readUTF();
            String playerName = input.readUTF();

            // Try fetching proxy player
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);

            MessageModule messageModule = ServerSelector.getInstance().getMessageModule();
            BungeeCommunicationModule communicationModule = instance.getCommunicationModule();

            // Check whether the player is already connected to the given server
            if (player.getServer().getInfo().getName().equalsIgnoreCase(server)) {
                // Send already connected message
                communicationModule.sendMessage(StorageKey.SEND_ALREADY_CONNECTED, player, messageModule, sender);
            }

            ServerInfo targetServer = ProxyServer.getInstance().getServerInfo(server);
            if (targetServer != null) {
                targetServer.ping(((result, error) -> {
                    if (error != null) {
                        communicationModule.sendMessage(StorageKey.SEND_SERVER_NOT_FOUND, player, messageModule, sender,
                                new Replacement("{server}", server)
                        );
                    } else {
                        communicationModule.sendMessage(StorageKey.SEND_PROCESSING, player,  messageModule, sender,
                                new Replacement("{server}", server)
                        );
                        player.connect(targetServer);
                    }
                }));
            } else {

                communicationModule.sendMessage(StorageKey.SEND_SERVER_NOT_FOUND, player, messageModule, sender,
                        new Replacement("{server}", server)
                );
            }
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in SendPlayerHandler.", exception);
        }
    }

    public void sendMessage(StorageKey key, ProxiedPlayer player, MessageModule messageModule, String sender, Replacement... replacements) {
        String[] messagePlayerRequest = {messageModule.convert(key, true, replacements), player.getName()};
        instance.getCommunicationModule().getHandler("MessagePlayer").process(messageModule.getDataInputStream(messagePlayerRequest), sender);
    }

}
