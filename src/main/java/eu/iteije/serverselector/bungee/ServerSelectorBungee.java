package eu.iteije.serverselector.bungee;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.bungee.cache.ClientCacheModule;
import eu.iteije.serverselector.bungee.files.BungeeFileModule;
import eu.iteije.serverselector.bungee.messaging.BungeeCommunicationModule;
import eu.iteije.serverselector.bungee.networking.SocketManager;
import eu.iteije.serverselector.bungee.queue.BungeeQueueManager;
import eu.iteije.serverselector.common.platform.Platform;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class ServerSelectorBungee extends Plugin {

    // Bungee module instances
    private BungeeCommunicationModule communicationModule;
    private BungeeFileModule fileModule;
    private ClientCacheModule clientCacheModule;
    private BungeeQueueManager queueManager;

    private SocketManager socketManager;

    // Bungee plugin instance
    @Getter private static ServerSelectorBungee instance;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;

        // If the plugin fails to make a new instance of ServerSelector it won't work -> shut down
        try {
            new ServerSelector(Platform.BUNGEE);
        } catch (Exception exception) {
            exception.printStackTrace();
            this.onDisable();
            return;
        }

        this.clientCacheModule = new ClientCacheModule(this);

        this.queueManager = new BungeeQueueManager(this);
        this.communicationModule = new BungeeCommunicationModule(this);
        this.getProxy().getPluginManager().registerListener(this, communicationModule);

        this.fileModule = new BungeeFileModule(this);

        this.socketManager = new SocketManager(this);
        socketManager.initializeSockets();
    }

    @Override
    public void onDisable() {
        ServerSelector.getInstance().disable();
    }
}
