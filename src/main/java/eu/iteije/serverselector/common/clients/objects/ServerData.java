package eu.iteije.serverselector.common.clients.objects;

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

}
