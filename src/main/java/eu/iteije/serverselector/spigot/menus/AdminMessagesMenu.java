package eu.iteije.serverselector.spigot.menus;

import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.services.menus.Item;
import eu.iteije.serverselector.spigot.services.menus.menu.Menu;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.Set;

public class AdminMessagesMenu extends Menu {

    public AdminMessagesMenu(ServerSelectorSpigot instance, int page) {
        super(instance, instance.getMessageModule().getMessage(StorageKey.MESSAGE_MENU_TITLE), 54);

        // Amount of slots available for messages
        int availableSlots = 36;
        // Slot where the
        int start = (availableSlots * page) - availableSlots;

        // Messages file
        FileConfiguration messagesFile = SpigotFileModule.getFileByName("messages.yml").getFileConfiguration();

        Set<String> messagesSet = messagesFile.getKeys(false);
        String[] messages = messagesSet.toArray(new String[messagesSet.size()]);

        if (messages.length <= availableSlots * page) availableSlots = messages.length -(availableSlots * (page - 1));

        for (int i = 0; i < availableSlots; i++) {
            setItem(start + i, new Item(Material.PAPER).setName("&7" + messages[start + i]).setLore(new String[]{new String(Objects.requireNonNull(messagesFile.getString(messages[start + i])))})
                    .onClick((player, item) -> {

                    }));
        }

        availableSlots = 36;

        // First 9 slots after the available slots
        for (int i = availableSlots; i < availableSlots + 9; i++) {
            setItem(i, new Item(Material.BLACK_STAINED_GLASS_PANE).setName("&0-").onClick((player, item) -> {}));
        }

        // Close menu item
        setItem(54 - 5, new Item(Material.REDSTONE_BLOCK).setName(StorageKey.MENU_CLOSE).onClick((player, item) -> {
            player.closeInventory();
        }));

        // Previous page item
        if (page > 1) {
            setItem(54 - 9, new Item(Material.ARROW).setName(StorageKey.MENU_PREVIOUS_PAGE).onClick((player, item) -> {
                AdminMessagesMenu newMenu = new AdminMessagesMenu(instance, page - 1);
                newMenu.open(player);
            }));
        }

        // Next page item
        if (messages.length > (availableSlots * page)) {
            setItem(54 - 1, new Item(Material.ARROW).setName(StorageKey.MENU_NEXT_PAGE).onClick((player, item) -> {
                AdminMessagesMenu newMenu = new AdminMessagesMenu(instance, page + 1);
                newMenu.open(player);
            }));
        }
    }

}
