package eu.iteije.serverselector.bungee.messaging.handlers;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeCommunicationImplementation;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.Server;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerInfoRequestHandler implements BungeeCommunicationImplementation {

    private ServerSelectorBungee instance;

    public ServerInfoRequestHandler(ServerSelectorBungee instance) {
        this.instance = instance;
    }

    @Override
    public void process(DataInputStream input, Connection connection) {
        try {
            // Read server name
            String server = input.readUTF();

            // If there is no ServerData element, the server won't return anything (which is handled by the spigot plugin)
            ServerData serverData = instance.getClientCacheModule().getServerData(server);

            if (serverData != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                DataOutputStream output = new DataOutputStream(bytes);

                output.writeUTF("ServerInfo");
                output.writeUTF(serverData.getServerName());
                output.writeUTF(serverData.getStatus());
                output.writeUTF(serverData.getCurrentPlayers());
                output.writeUTF(serverData.getMaxPlayers());

                String senderName = ((Server) connection).getInfo().getName();
                ServerInfo sender = ProxyServer.getInstance().getServerInfo(senderName);

                sender.sendData(MessageChannel.BUNGEE_GLOBAL.getChannel(), bytes.toByteArray());
            }

        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in ServerInfoRequest.", exception);
        }
    }
}
