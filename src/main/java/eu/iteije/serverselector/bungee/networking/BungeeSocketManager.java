package eu.iteije.serverselector.bungee.networking;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BungeeSocketManager {

    private ServerSelectorBungee instance;

    private HashMap<Integer, BungeeSocket> sockets;

    public BungeeSocketManager(ServerSelectorBungee instance) {
        this.instance = instance;

        this.sockets = new HashMap<>();
    }

    public void initializeSockets() {
        // Loop through all servers to open up a socket for every single server
        Map<String, ServerInfo> servers = instance.getProxy().getServers();
        for (ServerInfo server : servers.values()) {
            int port = server.getAddress().getPort() + 10000;
            new Thread(() -> {
                instance.getLogger().info("Server started. Listening on port " + port);
                sockets.put(port, new BungeeSocket(instance, new String[]{String.valueOf(port)}));
            }).start();
        }
    }

    public void initializeSocket(int port) {
        new Thread(() -> {
            instance.getLogger().info("Listening on port " + port);
            sockets.put(port, new BungeeSocket(instance, new String[]{String.valueOf(port)}));
        }).start();
    }

    public void cancelSocket(int port) {
        sockets.get(port).cancel();
    }

    public void closeSocket(int port) {
        try {
            if (sockets.get(port) != null) {
                if (sockets.get(port).getServerSocket() != null) {
                    sockets.get(port).getServerSocket().close();
                    ServerSelectorLogger.console("Closed socket on port " + port);
                }
            }
        } catch (IOException exception) {
            ServerSelectorLogger.console("Unable to close socket on port " + port);
        }
    }

    public void closeSockets() {
        for (BungeeSocket socket : sockets.values()) {
            try {
                if (socket.getServerSocket() != null) {
                    socket.getServerSocket().close();
                    ServerSelectorLogger.console("Closed socket on port " + socket.getPort());
                }

            } catch (IOException | NullPointerException exception) {
                ServerSelectorLogger.console("Unable to close socket on port " + socket.getPort());
            }
        }
    }

    public void renewSocket(BungeeSocket socket) {
        sockets.remove(socket.getPort());
        initializeSocket(socket.getPort());
    }


}
