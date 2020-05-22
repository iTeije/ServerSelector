package eu.iteije.serverselector.spigot.menus;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.services.menus.menu.Menu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class MenuModule {

    private ServerSelectorSpigot instance;

    // Selector info
    @Getter private ItemStack selectorItem;
    private Integer selectorSlot;
    @Getter @Setter private Boolean selectorItemEnabled;

    // Menu caching
    private HashMap<String, Menu> cachedMenus = new HashMap<>();

    public MenuModule(ServerSelectorSpigot serverSelectorSpigot) {
        this.instance = serverSelectorSpigot;

        saveSelectorItem();
        checkSelector();
    }

    public void saveSelectorItem() {
        String selectorItem = SpigotFileModule.getFile(StorageKey.CONFIG_SELECTOR_ITEM).getString(StorageKey.CONFIG_SELECTOR_ITEM);
        String selectorName = SpigotFileModule.getFile(StorageKey.CONFIG_SELECTOR_NAME).getString(StorageKey.CONFIG_SELECTOR_NAME);
        Integer selectorSlot = SpigotFileModule.getFile(StorageKey.CONFIG_SELECTOR_SLOT).getInt(StorageKey.CONFIG_SELECTOR_SLOT);

        ItemStack stack = new ItemStack(Material.valueOf(selectorItem));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ServerSelector.getInstance().getMessageModule().convertColorCodes(selectorName));
        stack.setItemMeta(meta);

        this.selectorItem = stack;
        this.selectorSlot = selectorSlot;
    }

    public void giveSelectorItem(Player player) {
        player.getInventory().setItem(this.selectorSlot, this.selectorItem);
    }

    private void checkSelector() {
        this.selectorItemEnabled = SpigotFileModule.getFile(StorageKey.CONFIG_SELECTOR).getBoolean(StorageKey.CONFIG_SELECTOR);
    }



    public void cacheMenu(Menu menu, String name) {
        this.cachedMenus.put(name, menu);
    }

    public Menu getCachedMenu(String name) {
        return this.cachedMenus.get(name);
    }

    public HashMap<String, Menu> getCachedMenus() {
        return this.cachedMenus;
    }

    public void deleteCachedMenus() {
        cachedMenus.clear();
    }

    public void deleteCachedMenu(String name) {
        this.cachedMenus.remove(name);
    }

}
