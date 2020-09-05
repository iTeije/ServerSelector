package eu.iteije.serverselector.bungee;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.bungee.cache.ClientCacheModule;
import eu.iteije.serverselector.bungee.files.BungeeFileModule;
import eu.iteije.serverselector.bungee.listeners.BungeeListenerModule;
import eu.iteije.serverselector.bungee.messaging.BungeeCommunicationModule;
import eu.iteije.serverselector.bungee.metrics.BungeeMetricsModule;
import eu.iteije.serverselector.bungee.networking.BungeeRedisManager;
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
    private BungeeListenerModule listenerModule;

    private BungeeRedisManager bungeeRedisManager;
    private BungeeMetricsModule metricsModule;

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

        this.fileModule = new BungeeFileModule(this);
        this.metricsModule = new BungeeMetricsModule(this, fileModule);

        this.clientCacheModule = new ClientCacheModule(this, metricsModule);

        this.queueManager = new BungeeQueueManager(this);
        this.communicationModule = new BungeeCommunicationModule(this);
        this.getProxy().getPluginManager().registerListener(this, communicationModule);

        this.listenerModule = new BungeeListenerModule(this);

        this.bungeeRedisManager = new BungeeRedisManager(this);
    }

    @Override
    public void onDisable() {
        bungeeRedisManager.getJedisPool().close();

        // Close influxdb connection
        if (this.metricsModule != null && this.metricsModule.getInflux() != null) {
            if (!this.metricsModule.getInflux().ping().getVersion().equalsIgnoreCase("unknown")) {
                this.metricsModule.getInflux().close();
            }
        }

        ServerSelector.getInstance().disable();
    }
}
