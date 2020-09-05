package eu.iteije.serverselector.spigot.metrics;

import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;

import java.util.concurrent.TimeUnit;

public class SpigotMetricsModule extends SpigotMetrics {

    @Getter private final InfluxDB influx;
    private final ServerSelectorSpigot instance;

    public SpigotMetricsModule(ServerSelectorSpigot instance) {
        super();
        this.instance = instance;

        String database = SpigotFileModule.getFile(StorageKey.INFLUX_DATABASE).getString(StorageKey.INFLUX_DATABASE);

        String url = SpigotFileModule.getFile(StorageKey.INFLUX_URL).getString(StorageKey.INFLUX_URL);
        String user = SpigotFileModule.getFile(StorageKey.INFLUX_USERNAME).getString(StorageKey.INFLUX_USERNAME);
        String password = SpigotFileModule.getFile(StorageKey.INFLUX_PASSWORD).getString(StorageKey.INFLUX_PASSWORD);

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
        Bukkit.getScheduler().runTaskLaterAsynchronously(instance, () -> {
            try {
                Point point = Point
                        .measurement("serverselector_spigot")
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .addField("redis_calls", getRedisCalls())
                        .build();

                this.influx.write(point);

                updateData();
            } catch (Exception exception) { // This was a database not found exception, but just for the sake of development...
                ServerSelectorLogger.console("Exception caught while collecting/updating data", exception);
                exception.printStackTrace();
                setEnabled(false);
            }
        }, 20 * 2L);
    }
}
