package eu.iteije.serverselector.common.networking.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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


    public boolean isAccessible() {
        return status.equalsIgnoreCase("ONLINE");
    }

}
