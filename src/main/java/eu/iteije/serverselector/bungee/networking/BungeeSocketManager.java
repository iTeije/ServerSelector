package eu.iteije.serverselector.bungee.networking;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.HashMap;
import java.util.Map;

public class BungeeSocketManager {

    private ServerSelectorBungee instance;

    private HashMap<Integer, BungeeSocketReworked> sockets;

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
                sockets.put(port, new BungeeSocketReworked(instance, new String[]{String.valueOf(port)}));
            }).start();
        }
    }

    public void cancelSocket(int port) {
        sockets.get(port).cancel();
    }


}
