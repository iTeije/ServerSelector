package eu.iteije.serverselector.spigot.messaging;

import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.handlers.MessagePlayerHandler;
import eu.iteije.serverselector.spigot.messaging.handlers.ServerInfoHandler;
import eu.iteije.serverselector.spigot.messaging.interfaces.SpigotCommunicationImplementation;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.HashMap;

public class SpigotCommunicationModule implements PluginMessageListener {

    private ServerSelectorSpigot serverSelectorSpigot;

    private HashMap<String, SpigotCommunicationImplementation> spigotHandlers = new HashMap<>();

    public SpigotCommunicationModule(ServerSelectorSpigot serverSelectorSpigot) {
        serverSelectorSpigot.getServer().getMessenger().registerOutgoingPluginChannel(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel());
        serverSelectorSpigot.getServer().getMessenger().registerIncomingPluginChannel(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), this);

        this.serverSelectorSpigot = serverSelectorSpigot;
        saveHandlers();
    }

    public SpigotCommunicationImplementation getHandler(String name) {
        return spigotHandlers.get(name);
    }

    public void addHandler(String name, SpigotCommunicationImplementation implementation) {
        spigotHandlers.put(name, implementation);
    }

    public void saveHandlers() {
        addHandler("ServerInfo", new ServerInfoHandler(serverSelectorSpigot));
        addHandler("MessagePlayer", new MessagePlayerHandler());
    }

    public void sendMessage(String context, String optional, String... playerNames) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);

        if (optional.equals("MessagePlayer")) {
            try {
                outputStream.writeUTF(optional);
                outputStream.writeUTF(context);
                for (String player : playerNames) {
                    outputStream.writeUTF(player);
                    serverSelectorSpigot.getServer().sendPluginMessage(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else if (optional.equals("Broadcast")) {
            try {
                outputStream.writeUTF(optional);
                outputStream.writeUTF(context);
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
            outputStream.writeUTF("SendPlayer");
            outputStream.writeUTF(server);
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
            outputStream.writeUTF("ServerInfoRequest");
            outputStream.writeUTF(server);
            serverSelectorSpigot.getServer().sendPluginMessage(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void queuePlayer(String server, Player player) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);

        try {
            outputStream.writeUTF("QueuePlayer");
            outputStream.writeUTF(player.getUniqueId().toString());
            serverSelectorSpigot.getServer().sendPluginMessage(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
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
            String type = inputStream.readUTF();

            SpigotCommunicationImplementation implementation = getHandler(type);
            if (implementation != null) implementation.process(inputStream);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
