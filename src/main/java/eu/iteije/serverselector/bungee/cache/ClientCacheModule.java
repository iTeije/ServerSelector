package eu.iteije.serverselector.bungee.cache;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import lombok.Getter;

import java.util.HashMap;

public class ClientCacheModule {

    @Getter private HashMap<String, ServerData> serverData = new HashMap<>();

    private ServerSelectorBungee instance;

    public ClientCacheModule(ServerSelectorBungee instance) {
        this.instance = instance;
    }

    public void updateServerData(ServerData data) {
        if (data.getStatus().equalsIgnoreCase("ONLINE")) {
            ServerData previous = serverData.get(data.getServerName());
            if (previous != null) {
                if (!previous.getStatus().equalsIgnoreCase(data.getStatus())) {
                    // Start sending people

                }
            } else {
                // Start sending people

            }
        }

        serverData.remove(data.getServerName());
        serverData.put(data.getServerName(), data);
    }

    public ServerData getServerData(String server) {
        return serverData.get(server);
    }
}
