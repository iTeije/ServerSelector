package eu.iteije.serverselector.bungee.networking;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class BungeeSocket {

    public BungeeSocket(ServerSelectorBungee instance, String[] args) {
        if (args.length < 1) {
            return;
        }

        int portNumber = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(portNumber, 0)) {
            Socket client = serverSocket.accept();
            DataInputStream in = new DataInputStream(client.getInputStream());

            // This is some dangerous shit
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

                        instance.getClientCacheModule().updateServerData(data);

                        serverSocket.close();

                        new Thread(() -> {
                            new BungeeSocket(instance, new String[]{String.valueOf(portNumber)});
                        }).start();
                    } catch (NullPointerException exception) {
                        exception.printStackTrace();
                    }
                    break;
            }
        } catch (IOException exception) {
            if (exception instanceof SocketException) {
                System.out.println("The server on port " + portNumber + " has disconnected, opening new socket on port " + portNumber + "...");
            } else {
                ServerSelectorLogger.console("Exception caught when trying to listen to port " + portNumber + ", opening new socket on port " + portNumber);
            }

            new Thread(() -> {
                new BungeeSocket(instance, new String[]{String.valueOf(portNumber)});
            }).start();
        }
    }
}
