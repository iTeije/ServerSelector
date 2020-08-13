package eu.iteije.serverselector.bungee.networking;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import lombok.Getter;
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

    private boolean active;

    @Getter private ServerSocket serverSocket;
    @Getter private final int port;

    private final ServerSelectorBungee instance;

    public void cancel() {
        this.active = false;
    }

    public BungeeSocket(ServerSelectorBungee instance, String[] args) {
        this.instance = instance;
        this.active = true;
        this.port = Integer.parseInt(args[0]);
        run(port);
    }

    public void run(int port) {
        try {
            serverSocket = new ServerSocket(port, 1);

            while (active) {
                Socket client = serverSocket.accept();

                DataInputStream in = new DataInputStream(client.getInputStream());

                // This is some dangerous shit
                String type = in.readUTF();
                String clientPort = in.readUTF();

                switch (type) {
                    case "serverinfo":
                        ServerSelectorLogger.console("Received serverinfo ping from port " + clientPort);

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
                            String[] whitelistedPlayers = in.readUTF().split(",");
                            String motd = in.readUTF();
                            String version = in.readUTF();
                            String tps = in.readUTF();
                            long uptime = in.readLong();
                            long currentMemory = in.readLong();
                            long maxMemory = in.readLong();

                            ServerData data = new ServerData(
                                    name,
                                    status,
                                    currentPlayers, maxPlayers,
                                    lastUpdate,
                                    instance.getQueueManager().getQueueSize(name), queueDelay,
                                    whitelistedPlayers,
                                    motd,
                                    version,
                                    tps,
                                    uptime,
                                    currentMemory, maxMemory
                            );

                            instance.getClientCacheModule().updateServerData(data);
                        } catch (NullPointerException exception) {
                            exception.printStackTrace();
                        }
                        break;
                }
            }

        } catch (IOException exception) {
            if (exception instanceof SocketException || exception instanceof EOFException) {
                instance.getBungeeSocketManager().closeSocket(this.port);

                ServerSelectorLogger.console("Client on port " + port + " disconnected. Opening new socket...");
                instance.getBungeeSocketManager().renewSocket(this);
            } else {
                exception.printStackTrace();
            }
        }

    }
}
