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
            Bukkit.broadcastMessage("glob: 1");
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
    public void onPluginMessageReceived(String channel, Player providedPlayer, byte[] data) {

        if (!channel.equals(MessageChannel.BUNGEE_GLOBAL.getChannel())) return;

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DataInputStream inputStream = new DataInputStream(input);

        try {
            String message = inputStream.readUTF();
            Bukkit.broadcastMessage("glob: " + message);
            boolean command = message.charAt(0) == '/';
            if (command) message = message.substring(1);
            if (data.length == 2) {
                Player player = Bukkit.getPlayer(inputStream.readUTF());
                if (command) {
                    player.performCommand(message);
                    return;
                }
                player.sendMessage(message);
            }
            if (command) {
                Bukkit.broadcastMessage("executing " + message + " as command");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), message);
                return;
            }
            Bukkit.broadcastMessage(message);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
