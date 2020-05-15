package eu.iteije.serverselector.spigot.players.listeners;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private ServerSelectorSpigot instance;

    public PlayerJoinListener(ServerSelectorSpigot serverSelectorSpigot) {
        this.instance = serverSelectorSpigot;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Joining player
        Player player = event.getPlayer();

        // Register new ServerSelectorPlayer
        instance.getPlayerModule().registerPlayer(player);

        // Give selector item whenever the item is enabled
        if (instance.getSelectorModule().getSelectorItemEnabled()) {
            instance.getSelectorModule().giveSelectorItem(player);
        }
    }

}
