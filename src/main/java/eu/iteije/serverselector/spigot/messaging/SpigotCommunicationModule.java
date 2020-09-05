package eu.iteije.serverselector.spigot.messaging;

import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.handlers.MessagePlayerHandler;
import eu.iteije.serverselector.spigot.messaging.handlers.ServerInfoHandler;
import eu.iteije.serverselector.spigot.messaging.handlers.UpdateMessageHandler;
import eu.iteije.serverselector.spigot.messaging.interfaces.SpigotHandlerImplementation;
import eu.iteije.serverselector.spigot.messaging.interfaces.SpigotRequestImplementation;
import eu.iteije.serverselector.spigot.messaging.requests.LeaveQueueRequest;
import eu.iteije.serverselector.spigot.messaging.requests.UpdateMessageRequest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.HashMap;

public class SpigotCommunicationModule implements PluginMessageListener {

    private ServerSelectorSpigot instance;

    private HashMap<String, SpigotHandlerImplementation> spigotHandlers = new HashMap<>();
    private HashMap<String, SpigotRequestImplementation> spigotRequests = new HashMap<>();

    public SpigotCommunicationModule(ServerSelectorSpigot serverSelectorSpigot) {
        serverSelectorSpigot.getServer().getMessenger().registerOutgoingPluginChannel(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel());
        serverSelectorSpigot.getServer().getMessenger().registerIncomingPluginChannel(serverSelectorSpigot, MessageChannel.BUNGEE_GLOBAL.getChannel(), this);

        this.instance = serverSelectorSpigot;
        saveHandlers();
        saveRequests();
    }

    public SpigotHandlerImplementation getHandler(String name) {
        return spigotHandlers.get(name);
    }

    public void addHandler(String name, SpigotHandlerImplementation implementation) {
        spigotHandlers.put(name, implementation);
    }

    public void saveHandlers() {
        addHandler("ServerInfo", new ServerInfoHandler(instance));
        addHandler("MessagePlayer", new MessagePlayerHandler());
        addHandler("UpdateMessage", new UpdateMessageHandler());
    }

    public SpigotRequestImplementation getRequest(String name) {
        return spigotRequests.get(name);
    }

    public void addRequest(String name, SpigotRequestImplementation implementation) {
        spigotRequests.put(name, implementation);
    }

    public void saveRequests() {
        addRequest("LeaveQueue", new LeaveQueueRequest(instance));
        addRequest("UpdateMessage", new UpdateMessageRequest(instance));
    }


    public void sendMessage(String context, String optional, String... playerNames) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);

        if (optional.equals("MessagePlayer")) {
            try {
                outputStream.writeUTF(optional);
                outputStream.writeUTF(context);
                Player[] players = Bukkit.getServer().getOnlinePlayers().toArray(new Player[]{});
                if (players.length > 0) {
                    for (String player : playerNames) {
                        outputStream.writeUTF(player);
                        players[0].sendPluginMessage(instance, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else if (optional.equals("Broadcast")) {
            try {
                outputStream.writeUTF(optional);
                outputStream.writeUTF(context);
                Player[] players = Bukkit.getServer().getOnlinePlayers().toArray(new Player[]{});
                if (players.length > 0) {
                    players[0].sendPluginMessage(instance, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
                }
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
            Player[] players = Bukkit.getServer().getOnlinePlayers().toArray(new Player[]{});
            if (players.length > 0) {
                players[0].sendPluginMessage(instance, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
            }
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
            Player[] players = Bukkit.getServer().getOnlinePlayers().toArray(new Player[]{});
            if (players.length > 0) {
                players[0].sendPluginMessage(instance, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void queuePlayer(String servers, Player player) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);

        try {
            outputStream.writeUTF("QueuePlayer");
            outputStream.writeUTF(player.getName());
            outputStream.writeUTF(servers);
            Player[] players = Bukkit.getServer().getOnlinePlayers().toArray(new Player[]{});
            if (players.length > 0) {
                players[0].sendPluginMessage(instance, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
            }
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

            SpigotHandlerImplementation implementation = getHandler(type);
            if (implementation != null) implementation.process(inputStream);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
