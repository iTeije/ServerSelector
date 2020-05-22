package eu.iteije.serverselector.bungee.networking;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Map;

public class SocketManager {

    private ServerSelectorBungee instance;

    public SocketManager(ServerSelectorBungee instance) {
        this.instance = instance;
    }

    public void initializeSockets() {
        // Loop through all servers to open up a socket for every single server
        Map<String, ServerInfo> servers = instance.getProxy().getServers();
        for (ServerInfo server : servers.values()) {
            int port = server.getAddress().getPort() + 10000;
            new Thread(() -> {
                new BungeeSocket(instance, new String[]{String.valueOf(port)});
            }).start();
        }
    }


}
