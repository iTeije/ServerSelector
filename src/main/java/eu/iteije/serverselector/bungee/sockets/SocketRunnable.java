package eu.iteije.serverselector.bungee.sockets;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.clients.objects.ServerData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Map;

public class SocketRunnable implements Runnable {

    private Socket client;
    private Socket receiver;
    private ServerSelectorBungee instance;

    public SocketRunnable(Socket client, Socket receiver, ServerSelectorBungee instance) {
        this.instance = instance;
        this.client = client;
        this.receiver = receiver;
    }

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(client.getInputStream());

            String type = in.readUTF();
            String clientPort = in.readUTF();
            switch (type) {
                case "serverinfo":
                    Map<String, ServerInfo> serverInfos = ProxyServer.getInstance().getServers();
                    ServerInfo clientInfo = null;
                    for (ServerInfo serverInfo : serverInfos.values()) {
                        if (String.valueOf(serverInfo.getAddress().getPort()).equals(clientPort)) {
                            clientInfo = serverInfo;
                        }
                    }
                    try {
                        String name = clientInfo.getName();
                        String status = in.readUTF();
                        String currentPlayers = in.readUTF();
                        String maxPlayers = in.readUTF();

                        ServerData data = new ServerData(
                                name, status, currentPlayers, maxPlayers
                        );

                        instance.getCommunicationModule().broadcast("name: " + name + ", status: " + status + ", curp:" + currentPlayers + ", maxp:" + maxPlayers);

                        instance.getClientCacheModule().updateServerData(data);
                    } catch (NullPointerException exception) {
                        exception.printStackTrace();
                    }
                    break;
            }
            in.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
}
