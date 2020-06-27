package eu.iteije.serverselector.spigot.runnables;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import lombok.Getter;

public class SpigotRunnableManager {

    @Getter private SelectorTimer selectorTimer;

    public SpigotRunnableManager(ServerSelectorSpigot instance) {
        this.selectorTimer = new SelectorTimer();
        instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, selectorTimer, 1000L, 50L);
    }
}
