package eu.iteije.serverselector.spigot.players.listeners;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private ServerSelectorSpigot instance;

    public PlayerQuitListener(ServerSelectorSpigot serverSelectorSpigot) {
        this.instance = serverSelectorSpigot;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Remove ServerSelectorPlayer from cache (just to save resources)
        instance.getPlayerModule().removePlayer(player.getUniqueId());

        // Removing the player using messaging channels is not always possible
        // because situations could occur where the quitting player is the last player
    }

}
