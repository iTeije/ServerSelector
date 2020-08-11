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
        if (instance.getMenuModule().getSelectorItemEnabled()) {
            instance.getMenuModule().giveSelectorItem(player);
            player.getInventory().setHeldItemSlot(instance.getMenuModule().getSelectorSlot());
        }

        // Check if the joining player has the permission to bypass the toggling of others
        if (!player.hasPermission("serverselector.actions.toggleplayers.bypass")) {
            // Loop through all players to hide the joining player from people who toggled players
            instance.getPlayerModule().players.values().forEach(onlinePlayer -> {
                if (onlinePlayer.isToggledPlayers()) onlinePlayer.getBukkitPlayer().hidePlayer(instance, player);
            });
        }

    }

}
