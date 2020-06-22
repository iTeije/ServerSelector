package eu.iteije.serverselector.bungee.messaging.handlers;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeHandlerImplementation;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ServerInfoRequestHandler implements BungeeHandlerImplementation {

    private ServerSelectorBungee instance;

    public ServerInfoRequestHandler(ServerSelectorBungee instance) {
        this.instance = instance;
    }

    @Override
    public void process(DataInputStream input, String sender) {
        try {
            // Read server name
            String server = input.readUTF().toLowerCase();

            // If there is no ServerData element, the server won't return anything (which is handled by the spigot plugin)
            ServerData serverData = instance.getClientCacheModule().getServerData(server);

            ServerInfo senderInfo = ProxyServer.getInstance().getServerInfo(sender);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(bytes);

            output.writeUTF("ServerInfo");

            if (serverData != null) {
                output.writeUTF(serverData.getServerName());
                output.writeUTF(serverData.getStatus());
                output.writeUTF(serverData.getCurrentPlayers());
                output.writeUTF(serverData.getMaxPlayers());
                output.writeLong(serverData.getLastUpdate());
                output.writeInt(serverData.getQueue());
                output.writeInt(serverData.getQueueDelay());
                output.writeUTF(Arrays.toString(serverData.getWhitelist()).replaceAll("[\\[\\](){}\\s]", ""));

                senderInfo.sendData(MessageChannel.BUNGEE_GLOBAL.getChannel(), bytes.toByteArray());
            } else {
                output.writeUTF(server);
                output.writeUTF("OFFLINE");
                output.writeUTF("0");
                output.writeUTF("0");
                output.writeLong(159103700L);
                output.writeInt(instance.getQueueManager().getQueueSize(server));
                output.writeInt(2);
                output.writeUTF("");

                senderInfo.sendData(MessageChannel.BUNGEE_GLOBAL.getChannel(), bytes.toByteArray());
            }

        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in ServerInfoHandler.", exception);
            exception.printStackTrace();
        }
    }
}
