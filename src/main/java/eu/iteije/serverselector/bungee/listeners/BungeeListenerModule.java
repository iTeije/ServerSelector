package eu.iteije.serverselector.bungee.listeners;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.listeners.players.PlayerDisconnectListener;

public class BungeeListenerModule {

    public BungeeListenerModule(ServerSelectorBungee instance) {
        instance.getProxy().getPluginManager().registerListener(instance, new PlayerDisconnectListener(instance));
    }
}
