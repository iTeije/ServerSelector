package eu.iteije.serverselector.spigot.players.listeners;

import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.services.menus.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryClickListener implements Listener {

    private boolean enabled;
    private ItemStack itemStack;

    public PlayerInventoryClickListener(ServerSelectorSpigot instance) {
        this.enabled = SpigotFileModule.getFile(StorageKey.CONFIG_SELECTOR_MOVE).getBoolean(StorageKey.CONFIG_SELECTOR_MOVE);
        this.itemStack = instance.getMenuModule().getSelectorItem();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getClickedInventory().getHolder() != null && event.getClickedInventory().getHolder() instanceof Menu) {
                event.setCancelled(true);

                Menu menu = (Menu) event.getClickedInventory().getHolder();
                menu.onClick(event.getSlot(), Bukkit.getPlayer(event.getWhoClicked().getName()));
            }

            if (!enabled) {
                ItemStack clicked = event.getCurrentItem();
                if (clicked != null) {
                    if (clicked.equals(itemStack)) {
                        event.setCancelled(true);
                        if (event.getWhoClicked() instanceof Player) {
                            Player player = (Player) event.getWhoClicked();
                            player.closeInventory();
                        }
                    }
                }
            }
        }
    }
}
