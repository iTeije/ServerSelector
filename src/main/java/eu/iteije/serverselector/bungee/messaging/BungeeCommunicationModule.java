package eu.iteije.serverselector.bungee.messaging;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
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

//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        DataOutputStream output = new DataOutputStream(bytes);
//
//        try {
//            output.writeUTF(MessageChannel.BUNGEE_GLOBAL.getChannel());
//            output.writeUTF(message);
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }

        BaseComponent[] components = TextComponent.fromLegacyText(message);

        player.sendMessage(ChatMessageType.CHAT, components);
    }

    public void broadcast(String message) {
        ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText(message));
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals(MessageChannel.BUNGEE_GLOBAL.getChannel())) return;
        if (!(event.getSender() instanceof Server)) return;

        ByteArrayInputStream stream = new ByteArrayInputStream(event.getData());
        DataInputStream inputStream = new DataInputStream(stream);
        try {
            String message = inputStream.readUTF();
            String optional = inputStream.readUTF();

            if (optional.equals("")) {
                String username = inputStream.readUTF();

                // Pass data to all subservers
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(username);

                sendTargeted(player, message);
            } else if (optional.equals("broadcast")) {
                broadcast(message);
            }

            serverSelectorBungee.getLogger().info(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
