package eu.iteije.serverselector.common.networking.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.UUID;

@Getter
@Setter
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


    public boolean isAccessible(UUID uuid) {
        if (status.equalsIgnoreCase("WHITELISTED") || Arrays.toString(whitelist).contains(uuid.toString())) return true;
        return status.equalsIgnoreCase("ONLINE");
    }

}
