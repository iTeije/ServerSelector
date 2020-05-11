package eu.iteije.serverselector.spigot.services.menus.menu;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.spigot.services.menus.Item;
import eu.iteije.serverselector.spigot.services.menus.managers.InventoryManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Menu implements InventoryHolder {

    @Getter private Inventory inventory;
    private Map<Integer, Item> slotMap = new HashMap<>();

    public Menu(JavaPlugin javaPlugin, String name, int size) {
        inventory = Bukkit.createInventory(this, size, ServerSelector.getInstance().getMessageModule().convert(name));
        InventoryManager.getInstance(javaPlugin);
    }

    public void onClose(Player player) {

    }

    public Menu setItem(int slot, Item item) {
        slotMap.put(slot, item);
        inventory.setItem(slot, item.getItem());
        return this;
    }

    public void onClick(int slot, Player player) {
        Item clicked = slotMap.get(slot);
        if (clicked == null) return;

        clicked.getOnClick().accept(player, clicked);
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
    }
}
