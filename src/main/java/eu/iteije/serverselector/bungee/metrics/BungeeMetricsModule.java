package eu.iteije.serverselector.bungee.metrics;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.files.BungeeFileModule;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import lombok.Getter;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;

import java.util.concurrent.TimeUnit;

public class BungeeMetricsModule extends BungeeMetrics {

    @Getter
    private InfluxDB influx;
    private final ServerSelectorBungee instance;

    public BungeeMetricsModule(ServerSelectorBungee instance) {
        super();
        this.instance = instance;

        boolean enabled = BungeeFileModule.getFile(StorageKey.METRICS_ENABLED).getBoolean(StorageKey.METRICS_ENABLED);
        if (!enabled) {
            ServerSelectorLogger.console("Cancelling startup of BungeeMetricsModule.");
            return;
        }

        String database = BungeeFileModule.getFile(StorageKey.INFLUX_DATABASE).getString(StorageKey.INFLUX_DATABASE);

        String url = BungeeFileModule.getFile(StorageKey.INFLUX_URL).getString(StorageKey.INFLUX_URL);
        String user = BungeeFileModule.getFile(StorageKey.INFLUX_USERNAME).getString(StorageKey.INFLUX_USERNAME);
        String password = BungeeFileModule.getFile(StorageKey.INFLUX_PASSWORD).getString(StorageKey.INFLUX_PASSWORD);

        this.influx = InfluxDBFactory.connect(url, user, password);

        Pong response = this.influx.ping();
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            ServerSelectorLogger.console("InfluxDB connection couldn't be established, disabling metrics...");
            return;
        }

        this.influx.setLogLevel(InfluxDB.LogLevel.NONE);
        this.influx.enableGzip();
        if (!this.influx.databaseExists(database)) this.influx.createDatabase(database);
        this.influx.setRetentionPolicy("autogen");
        this.influx.setDatabase(database);

        setEnabled(true);

        // Initialize async scheduler
        updateData();
    }

    private void updateData() {
        instance.getProxy().getScheduler().schedule(instance, () -> {
            try {
                // Proxy data
                Point proxyData = Point
                        .measurement("serverselector_bungee")
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .addField("redis_calls", getRedisCalls())
                        .build();

                // Spigot data
                Point spigotData = Point
                        .measurement("serverselector_spigot")
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .addField("redis_calls", getSpigotRedisCalls())
                        .build();

                this.influx.write(proxyData);
                this.influx.write(spigotData);

                updateData();
            } catch (Exception exception) { // This was a database not found exception, but just for the sake of development...
                ServerSelectorLogger.console("Exception caught while collecting/updating data", exception);
                exception.printStackTrace();
                setEnabled(false);
            }
        }, 2000, TimeUnit.MILLISECONDS);
    }

}
