package eu.iteije.serverselector.spigot.services.menus.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryInteractionListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof Menu) {
            Menu menu = (Menu) event.getInventory().getHolder();
            menu.onClose(Bukkit.getPlayer(event.getPlayer().getName()));
        }
    }

}
