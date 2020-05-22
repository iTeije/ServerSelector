package eu.iteije.serverselector.bungee.messaging;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import eu.iteije.serverselector.common.messaging.MessageModule;
import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.storage.StorageKey;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.Collection;

public class BungeeCommunicationModule implements Listener {

    private ServerSelectorBungee serverSelectorBungee;

    public BungeeCommunicationModule(ServerSelectorBungee serverSelectorBungee) {
        this.serverSelectorBungee = serverSelectorBungee;

        serverSelectorBungee.getProxy().registerChannel(MessageChannel.BUNGEE_GLOBAL.getChannel());
    }

    public void sendTargeted(ProxiedPlayer player, String message) {
        // Get online players from all subservers
        Collection<ProxiedPlayer> proxiedPlayers = ProxyServer.getInstance().getPlayers();

        // Make sure there is a player online
        if (proxiedPlayers == null || proxiedPlayers.isEmpty()) return;

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);

        try {
            output.writeUTF(message);
            output.writeUTF(player.getName());
            player.getServer().getInfo().sendData(MessageChannel.BUNGEE_GLOBAL.getChannel(), bytes.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void broadcast(String message) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);

        try {
            output.writeUTF(message);
            ProxyServer.getInstance().getServers().values().forEach(server -> server.sendData(MessageChannel.BUNGEE_GLOBAL.getChannel(), bytes.toByteArray()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void sendPlayer(String server, String playerName) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);

        MessageModule messageModule = ServerSelector.getInstance().getMessageModule();

        // Send 'summon' message regardless of the server the player is currently on
        sendTargeted(player, messageModule.convert(StorageKey.SEND_PROCESSING, true,
                new Replacement("{server}", server)
                ));

        // Check if the player is already connected to the server
        if (player.getServer().getInfo().getName().equalsIgnoreCase(server)) {
            sendTargeted(player, messageModule.convert(StorageKey.SEND_ALREADY_CONNECTED, true));
        }

        ServerInfo targetServer = ProxyServer.getInstance().getServerInfo(server);
        if (targetServer != null) {
            targetServer.ping((result, error) -> {
                if (error != null) {
                    sendTargeted(player, messageModule.convert(StorageKey.SEND_SERVER_NOT_FOUND, true,
                            new Replacement("{server}", server)
                    ));
                } else {
                    player.connect(targetServer);
                }
            });
        } else {
            sendTargeted(player, messageModule.convert(StorageKey.SEND_SERVER_NOT_FOUND, true,
                    new Replacement("{server}", server)
                    ));
        }

    }



    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals(MessageChannel.BUNGEE_GLOBAL.getChannel())) return;
        if (!(event.getSender() instanceof Server)) return;

        ByteArrayInputStream stream = new ByteArrayInputStream(event.getData());
        DataInputStream inputStream = new DataInputStream(stream);
        try {
            String context = inputStream.readUTF();
            String optional = inputStream.readUTF();

            if (optional.equals("")) {
                String username = inputStream.readUTF();

                // Pass data to all subservers
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(username);

                sendTargeted(player, context);
            } else if (optional.equals("broadcast")) {
                broadcast(context);
            } else if (optional.equals("send")) {
                String playerName = inputStream.readUTF();
                sendPlayer(context, playerName);
            } else if (optional.equals("serverinforequest")) {
                // context = server name
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                DataOutputStream output = new DataOutputStream(bytes);

                try {
                    output.writeUTF("serverinfo");

                    // If there's no cached ServerData element, the server won't return anything (which is handled by the spigot plugin)
                    String requester = ((Server) event.getSender()).getInfo().getName();
                    ServerData serverData = serverSelectorBungee.getClientCacheModule().getServerData(context);

                    if (serverData != null) {
                        output.writeUTF(serverData.getServerName());
                        output.writeUTF(serverData.getStatus());
                        output.writeUTF(serverData.getCurrentPlayers());
                        output.writeUTF(serverData.getMaxPlayers());

                        ServerInfo client = ProxyServer.getInstance().getServerInfo(requester);

                        client.sendData(MessageChannel.BUNGEE_GLOBAL.getChannel(), bytes.toByteArray());
                        return;

                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
