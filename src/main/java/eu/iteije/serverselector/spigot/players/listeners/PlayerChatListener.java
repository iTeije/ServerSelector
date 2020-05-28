package eu.iteije.serverselector.spigot.players.listeners;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.players.ServerSelectorPlayer;
import eu.iteije.serverselector.spigot.services.actionqueue.Action;
import eu.iteije.serverselector.spigot.services.actionqueue.ActionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private ServerSelectorSpigot instance;

    public PlayerChatListener(ServerSelectorSpigot instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        ServerSelectorPlayer player = instance.getPlayerModule().getPlayer(event.getPlayer().getUniqueId());
        if (player.hasAction(ActionType.CHAT)) {
            Action action = player.getAction(ActionType.CHAT);
            if (event.getMessage().toLowerCase().equalsIgnoreCase("cancel")) {
                action.cancel();
            } else {
                action.execute(event.getMessage());
            }
            event.setCancelled(true);
        }
    }

}
