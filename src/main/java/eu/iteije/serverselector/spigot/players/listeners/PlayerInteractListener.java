package eu.iteije.serverselector.spigot.players.listeners;

import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import eu.iteije.serverselector.spigot.services.menus.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;

public class PlayerInteractListener implements Listener {

    private ServerSelectorSpigot instance;

    public PlayerInteractListener(ServerSelectorSpigot instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (Objects.equals(event.getHand(), EquipmentSlot.HAND)) {
            if (player.getInventory().getItemInMainHand().equals(instance.getMenuModule().getSelectorItem())) {
                if (instance.getMenuModule().getSelectorItemEnabled()) {
                    SpigotMessageModule messageModule = instance.getMessageModule();

                    Menu cachedMenu = instance.getMenuModule().getCachedMenu("Main");

                    messageModule.sendToPlayer(StorageKey.MENU_OPENING, new Player[]{player}, MessageType.MESSAGE,
                            new Replacement("{menu}", cachedMenu.getChatName())
                    );

                    cachedMenu.open(player);
                }
            }
        }

    }
}
