package eu.iteije.serverselector.spigot.messaging;

import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.selector.SelectorModule;
import eu.iteije.serverselector.spigot.selector.objects.ServerInfo;
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

    public void sendMessage(String context, String optional, String... playerNames) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);

        if (optional.equals("")) {
            try {
                outputStream.writeUTF(context);
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
                outputStream.writeUTF(context);
                outputStream.writeUTF(optional);
                serverSelectorSpigot.getServer().sendPluginMessage(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void sendPlayer(String server, String playerName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);

        try {
            outputStream.writeUTF(server);
            outputStream.writeUTF("send");
            outputStream.writeUTF(playerName);
            serverSelectorSpigot.getServer().sendPluginMessage(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void requestServerInfo(String server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);

        try {
            outputStream.writeUTF(server);
            outputStream.writeUTF("serverinforequest");
            serverSelectorSpigot.getServer().sendPluginMessage(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void sendServerInfo(String server) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(bytes);
            // Getting information of the current server
            output.writeUTF("context");
            // Making clear its server information
            output.writeUTF("serverinfo");
            // Requester server name
            output.writeUTF(server);
            // Status
            if (serverSelectorSpigot.getServer().hasWhitelist()) {
                output.writeUTF("WHITELISTED");
            } else {
                output.writeUTF("ONLINE");
            }
            // Current players
            output.writeUTF(String.valueOf(serverSelectorSpigot.getServer().getOnlinePlayers().size()));
            // Max players
            output.writeUTF(String.valueOf(serverSelectorSpigot.getServer().getMaxPlayers()));
            Bukkit.broadcastMessage("SpigotCommunicationModule: returning server information");
            serverSelectorSpigot.getServer().sendPluginMessage(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), bytes.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player providedPlayer, byte[] data) {

        if (!channel.equals(MessageChannel.BUNGEE_GLOBAL.getChannel())) return;

        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DataInputStream inputStream = new DataInputStream(input);

        try {
            String message = inputStream.readUTF();
            if (message.equals("serverinforequest")) {
                sendServerInfo(inputStream.readUTF());
                return;
            } else if (message.equals("serverinfo")) {
                Bukkit.broadcastMessage("SpigotCommunicationModule: processing received information");
                SelectorModule selectorModule = serverSelectorSpigot.getSelectorModule();
                ServerInfo serverInfo = new ServerInfo(
                        inputStream.readUTF(),
                        inputStream.readUTF(),
                        inputStream.readUTF(),
                        inputStream.readUTF()
                );
                selectorModule.getMenuUpdater().updateServerInfo(serverInfo);
                return;
            }
            boolean command = message.charAt(0) == '/';
            if (command) message = message.substring(1);
            if (data.length == 2) {
                Player player = Bukkit.getPlayer(inputStream.readUTF());
                if (command) {
                    player.performCommand(message);
                    return;
                }
                player.sendMessage(message);
                return;
            }
            if (command) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), message);
                return;
            }
            Bukkit.broadcastMessage(message);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
