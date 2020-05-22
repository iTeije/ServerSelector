package eu.iteije.serverselector.bungee.cache;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.clients.objects.ServerData;
import lombok.Getter;

import java.util.HashMap;

public class ClientCacheModule {

    @Getter private HashMap<String, ServerData> serverData = new HashMap<>();

    private ServerSelectorBungee instance;

    public ClientCacheModule(ServerSelectorBungee instance) {
        this.instance = instance;
    }

    public void updateServerData(ServerData data) {
        serverData.remove(data.getServerName());
        serverData.put(data.getServerName(), data);
        instance.getCommunicationModule().broadcast("--> Succesfully imported server data");
    }

    public ServerData getServerData(String server) {
        return serverData.get(server);
    }
}