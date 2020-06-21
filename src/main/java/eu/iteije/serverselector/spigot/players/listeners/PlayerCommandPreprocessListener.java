package eu.iteije.serverselector.spigot.players.listeners;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerCommandPreprocessListener implements Listener {

    ServerSelectorSpigot instance;

    public PlayerCommandPreprocessListener(ServerSelectorSpigot instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        // If whitelist is being turned on or off, the server should send a updater ServerData regardless of the current
        // update scheduler delay and current whitelist status
        String command = event.getMessage();
        if (event.getPlayer().hasPermission("minecraft.command.whitelist")) {
            if (command.contains("whitelist")) {
                Map<String, String> force = new HashMap<>();
                if (command.equalsIgnoreCase("/whitelist on")) {
                    force.put("status", "whitelisted");
                    instance.getSelectorModule().getStatusUpdater().updateServerInfo(force);
                } else if (command.equalsIgnoreCase("/whitelist off")) {
                    force.put("status", "online");
                    instance.getSelectorModule().getStatusUpdater().updateServerInfo(force);
                }
                force = null;
            }
        }
    }
}
