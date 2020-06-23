package eu.iteije.serverselector.spigot.runnables;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import lombok.Getter;

@Getter
public class SpigotRunnables {

    public ServerSelectorTimer timer;

    public SpigotRunnables(ServerSelectorSpigot instance) {
        this.timer = new ServerSelectorTimer(instance);
    }
}
