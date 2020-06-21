package eu.iteije.serverselector.spigot.players.listeners;

import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDropItemListener implements Listener {

    private boolean enabled;
    private ItemStack itemStack;

    public PlayerDropItemListener(ServerSelectorSpigot instance) {
        this.enabled = SpigotFileModule.getFile(StorageKey.CONFIG_SELECTOR_MOVE).getBoolean(StorageKey.CONFIG_SELECTOR_MOVE);
        this.itemStack = instance.getMenuModule().getSelectorItem();
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (!enabled) {
            ItemStack clicked = event.getItemDrop().getItemStack();
            if (clicked.equals(itemStack)) {
                event.setCancelled(true);
            }
        }
    }
}
