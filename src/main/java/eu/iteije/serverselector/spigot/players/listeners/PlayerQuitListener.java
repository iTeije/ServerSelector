package eu.iteije.serverselector.spigot.players.listeners;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private ServerSelectorSpigot instance;

    public PlayerQuitListener(ServerSelectorSpigot serverSelectorSpigot) {
        this.instance = serverSelectorSpigot;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove ServerSelectorPlayer from cache (just to save resources)
        instance.getPlayerModule().removePlayer(event.getPlayer().getUniqueId());
    }

}
