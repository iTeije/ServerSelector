package eu.iteije.serverselector.bungee.messaging.handlers;

import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeCommunicationImplementation;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessagePlayerHandler implements BungeeCommunicationImplementation {

    public MessagePlayerHandler() {

    }

    @Override
    public void process(DataInputStream input, Connection connection) {
        try {
            // Read and define message
            String message = input.readUTF();
            // Read and define given playername
            String playerName = input.readUTF();

            // Try getting the proxied player from the playername
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(bytes);

            // Define spigot request type as first value in the output
            output.writeUTF("MessagePlayer");
            output.writeUTF(message);
            output.writeUTF(player.getName());
            player.getServer().getInfo().sendData(MessageChannel.BUNGEE_GLOBAL.getChannel(), bytes.toByteArray());
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in MessagePlayerRequest.", exception);
        }
    }
}
