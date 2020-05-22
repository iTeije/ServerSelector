package eu.iteije.serverselector.bungee.networking;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.clients.objects.ServerData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

public class BungeeSocket {

    public BungeeSocket(ServerSelectorBungee instance, String[] args) {
        if (args.length < 1) {
            System.out.println("instance = " + instance + ", args = " + Arrays.deepToString(args));
            return;
        }

        System.out.println("Server started. Listening on port " + args[0]);
        int portNumber = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(portNumber, 0)) {
            Socket client = serverSocket.accept();
            DataInputStream in = new DataInputStream(client.getInputStream());

            System.out.println("Client connected on port " + portNumber + ". Servicing requests.");

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

                        instance.getCommunicationModule().broadcast("name: " + name + ", status: " + status + ", curp:" + currentPlayers + ", maxp:" + maxPlayers);

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
            System.out.println("Exception caught when trying to listen on port " + portNumber);
            exception.printStackTrace();
        }


//        try {
//            ServerSocket serverSocket = new ServerSocket(25577);
//            Socket socket = serverSocket.accept();
//            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
//
//            // This will be some type of enum and abstract functions in the future
//            String messageType = dataInputStream.readUTF();
//            String clientIp = dataInputStream.readUTF();
//            String clientPort = dataInputStream.readUTF();
//
//            switch (messageType) {
//                case "serverinfo":
//                    Map<String, ServerInfo> serverInfos = ProxyServer.getInstance().getServers();
//                    ServerInfo client = null;
//                    for (ServerInfo serverInfo : serverInfos.values()) {
//                        instance.getCommunicationModule().broadcast("Client hostname: " + serverInfo.getAddress().getHostName());
//                        instance.getCommunicationModule().broadcast("Client address: " + serverInfo.getAddress().getAddress());
//                        instance.getCommunicationModule().broadcast("Client port: " + serverInfo.getAddress().getPort());
//                        if (serverInfo.getAddress().getHostName().equals(clientIp) && String.valueOf(serverInfo.getAddress().getPort()).equals(clientPort)) {
//                            client = serverInfo;
//                        }
//                    }
//                    try {
//                        String name = client.getName();
//                        String status = dataInputStream.readUTF();
//                        String currentPlayers = dataInputStream.readUTF();
//                        String maxPlayers = dataInputStream.readUTF();
//
//                        ServerData data = new ServerData(
//                                name, status, currentPlayers, maxPlayers
//                        );
//
//                        instance.getClientCacheModule().updateServerData(data);
//                    } catch (NullPointerException exception) {
//                        exception.printStackTrace();
//                    }
//                    break;
//                default:
//                    break;
//            }
//
//            dataInputStream.close();
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
    }
}
