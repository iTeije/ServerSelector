package eu.iteije.serverselector.bungee.networking;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.DataInputStream;
import java.io.EOFException;
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

        ServerSelectorLogger.console("New socket instance, listening on " + portNumber);

        try (ServerSocket serverSocket = new ServerSocket(portNumber, 0)) {
            ServerSelectorLogger.console("Connection accepted");
            Socket client = serverSocket.accept();
            ServerSelectorLogger.console("Accepted request");
            DataInputStream in = new DataInputStream(client.getInputStream());

            // This is some dangerous shit
            String type = in.readUTF();
            String clientPort = in.readUTF();

            serverSocket.close();

            new Thread(() -> {
                new BungeeSocket(instance, new String[]{String.valueOf(portNumber)});
            }).start();

            switch (type) {
                case "serverinfo":
                    ServerSelectorLogger.console("ServerInfo updater");
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
                        long lastUpdate = in.readLong();
                        int queueDelay = in.readInt();

                        ServerData data = new ServerData(
                                name,
                                status,
                                currentPlayers,
                                maxPlayers,
                                lastUpdate,
                                instance.getQueueManager().getQueueSize(name),
                                queueDelay
                        );

                        instance.getClientCacheModule().updateServerData(data);
                    } catch (NullPointerException exception) {
                        exception.printStackTrace();
                    }
                    break;
            }
        } catch (IOException exception) {
            if (exception instanceof SocketException) {
                System.out.println("The server on port " + portNumber + " has disconnected, opening new socket on port " + portNumber + "...");
            } else if (exception instanceof EOFException) {
                // Ignore it, since it's not important for now and it's just opening a new socket
            } else {
                ServerSelectorLogger.console("Exception caught when trying to listen to port " + portNumber + ", opening new socket on port " + portNumber);
                exception.printStackTrace();
            }

            exception.printStackTrace();

            new Thread(() -> {
                new BungeeSocket(instance, new String[]{String.valueOf(portNumber)});
            }).start();
        }
    }
}
