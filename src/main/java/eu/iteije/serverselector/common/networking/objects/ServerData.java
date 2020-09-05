package eu.iteije.serverselector.common.networking.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ServerData {

    public String serverName;
    public String status;
    public String currentPlayers;
    public String maxPlayers;
    public long lastUpdate;

    public int queue;
    public int queueDelay;

    public String[] whitelist;

    // Item lores update
    public  String motd;
    public String version;
    public String tps;
    public long uptime;
    public long currentMemoryUsage;
    public long maxMemoryUsage;

    public Integer redisCalls;



    public boolean isAccessible(UUID uuid) {
        if (status.equalsIgnoreCase("WHITELISTED") || Arrays.toString(whitelist).contains(uuid.toString())) return true;
        return status.equalsIgnoreCase("ONLINE");
    }

}
