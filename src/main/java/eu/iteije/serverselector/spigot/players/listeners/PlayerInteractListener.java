package eu.iteije.serverselector.spigot.players.listeners;

import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.enums.ReplacementType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private ServerSelectorSpigot instance;

    public PlayerInteractListener(ServerSelectorSpigot instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem().equals(instance.getMenuModule().getSelectorItem())) {
            if (instance.getMenuModule().getSelectorItemEnabled()) {
                SpigotMessageModule messageModule = instance.getMessageModule();

                messageModule.sendToPlayer(StorageKey.MENU_OPENING, new Player[]{player}, MessageType.MESSAGE,
                        new Replacement("{menu}", "Selector", ReplacementType.VARIABLE)
                );
            }
        }
    }
}
