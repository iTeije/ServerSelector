package eu.iteije.serverselector.bungee.messaging.handlers;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeHandlerImplementation;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import net.md_5.bungee.api.ProxyServer;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class BroadcastHandler implements BungeeHandlerImplementation {

    private ServerSelectorBungee instance;

    public BroadcastHandler(ServerSelectorBungee instance) {
        this.instance = instance;
    }

    @Override
    public void process(DataInputStream input, String sender) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(bytes);

            // Write message
            String message = input.readUTF();
            output.writeUTF("MessagePlayer");
            output.writeUTF(message);

            // Check which servers have more than 1 player online, since we don't want a spam of broadcast messages once the first player connects
            Map<String, ServerData> serverData = instance.getClientCacheModule().getServerData();

            ProxyServer.getInstance().getServers().values().forEach(server -> {
                if (message.charAt(0) == '/') {
                    server.sendData(MessageChannel.BUNGEE_GLOBAL.getChannel(), bytes.toByteArray());
                }
                ServerData data = serverData.get(server.getName());
                if (data != null) {
                    if (Integer.parseInt(data.getCurrentPlayers()) > 0) {
                        server.sendData(MessageChannel.BUNGEE_GLOBAL.getChannel(), bytes.toByteArray());
                    }
                }
            });
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in BroadcastHandler.", exception);
        }
    }
}
