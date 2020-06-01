package eu.iteije.serverselector.bungee.networking;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class BungeeSocketReworked {

    private boolean active;

    private ServerSocket serverSocket;
    private Socket client;
    private PrintStream output;
    private BufferedReader input;

    private ServerSelectorBungee instance;

    public void cancel() {
        this.active = false;
    }

    public BungeeSocketReworked(ServerSelectorBungee instance, String[] args) {
        this.instance = instance;
        run(Integer.parseInt(args[0]));
    }

    public void run(int port) {
        try {
            serverSocket = new ServerSocket(port, 1);
            client = serverSocket.accept();

            output = new PrintStream(client.getOutputStream());
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (client.isConnected()) {
                // This is some dangerous shit
                String type = input.readLine();
                String clientPort = input.readLine();

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
                            String status = input.readLine();
                            String currentPlayers = input.readLine();
                            String maxPlayers = input.readLine();
                            long lastUpdate = Long.parseLong(input.readLine());
                            int queueDelay = Integer.parseInt(input.readLine());

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
            }



        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
