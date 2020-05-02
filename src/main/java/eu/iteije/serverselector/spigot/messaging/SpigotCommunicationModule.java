package eu.iteije.serverselector.spigot.messaging;

import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import org.bukkit.Bukkit;
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

    public void sendMessage(String message, Player player) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);
        try {
            outputStream.writeUTF(message);
            outputStream.writeUTF(player.getName());
            outputStream.writeUTF("");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        serverSelectorSpigot.getServer().sendPluginMessage(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        if (!channel.equals(MessageChannel.BUNGEE_GLOBAL.getChannel())) return;

        ByteArrayInputStream input = new ByteArrayInputStream(message);
        DataInputStream inputStream = new DataInputStream(input);
        try {
            String receivedChannel = inputStream.readUTF();
            String receivedMessage = inputStream.readUTF();

            Bukkit.broadcastMessage(receivedMessage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


}
