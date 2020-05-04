package eu.iteije.serverselector.spigot.messaging;

import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;

public class SpigotCommunicationModule implements PluginMessageListener {

    private ServerSelectorSpigot serverSelectorSpigot;

    public SpigotCommunicationModule(ServerSelectorSpigot serverSelectorSpigot) {
        serverSelectorSpigot.getServer().getMessenger().registerOutgoingPluginChannel(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel());
        serverSelectorSpigot.getServer().getMessenger().registerIncomingPluginChannel(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), this);

        this.serverSelectorSpigot = serverSelectorSpigot;
    }

    public void sendMessage(String message, String optional, String... playerNames) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);

        if (optional.equals("")) {
            try {
                outputStream.writeUTF(message);
                outputStream.writeUTF(optional);
                for (String player : playerNames) {
                    outputStream.writeUTF(player);
                    serverSelectorSpigot.getServer().sendPluginMessage(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else if (optional.equals("broadcast")) {
            try {
                outputStream.writeUTF(message);
                outputStream.writeUTF(optional);
                serverSelectorSpigot.getServer().sendPluginMessage(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        if (!channel.equals(MessageChannel.BUNGEE_GLOBAL.getChannel())) return;

        ByteArrayInputStream input = new ByteArrayInputStream(message);
        DataInputStream inputStream = new DataInputStream(input);
        try {
            String receivedChannel = inputStream.readUTF();
            String receivedMessage = inputStream.readUTF();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


}
