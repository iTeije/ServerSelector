package eu.iteije.serverselector.spigot.selector.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServerInfo {

    public String serverName;
    public String status;
    public String currentPlayers;
    public String maxPlayers;

}
