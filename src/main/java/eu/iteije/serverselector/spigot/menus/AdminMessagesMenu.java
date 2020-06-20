package eu.iteije.serverselector.spigot.menus;

import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import eu.iteije.serverselector.spigot.players.ServerSelectorPlayer;
import eu.iteije.serverselector.spigot.services.actionqueue.Action;
import eu.iteije.serverselector.spigot.services.actionqueue.ActionType;
import eu.iteije.serverselector.spigot.services.menus.Item;
import eu.iteije.serverselector.spigot.services.menus.menu.Menu;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class AdminMessagesMenu extends Menu {

    public AdminMessagesMenu(ServerSelectorSpigot instance, int page) {
        super(instance, instance.getMessageModule().getMessage(StorageKey.MESSAGE_MENU_TITLE), 54);

        // Amount of slots available for messages
        int availableSlots = 36;
        // Slot where the
        int start = (availableSlots * page) - availableSlots;

        // Messages module
        SpigotMessageModule messageModule = instance.getMessageModule();

        // Messages file
        FileConfiguration messagesFile = SpigotFileModule.getFileByName("messages.yml").getFileConfiguration();

        Set<String> messagesSet = messagesFile.getKeys(false);
        String[] messages = messagesSet.toArray(new String[messagesSet.size()]);

        if (messages.length <= availableSlots * page) availableSlots = messages.length -(availableSlots * (page - 1));

        for (int i = 0; i < availableSlots; i++) {
            int finalI = i;
            setItem(start + i, new Item(Material.PAPER).setName("&7" + messages[start + i]).setLore(new String[]{Objects.requireNonNull(messagesFile.getString(messages[start + i]))}).onClick((player, item) -> {
                messageModule.sendToPlayer(StorageKey.ACTIONQUEUE_PROPOSE_CHAT, new Player[]{player}, MessageType.MESSAGE);
                ServerSelectorPlayer selectorPlayer = instance.getPlayerModule().getPlayer(player.getUniqueId());
                selectorPlayer.queueAction(new Action(instance, player, ActionType.CHAT).onExecute((action, s) -> {
                    String[] words = s.split("\\s+");

                    words = Arrays.copyOfRange(words, 0, words.length);

                    String message = String.join(" ", words);

                    instance.getCommunicationModule().getRequest("UpdateMessage").process(messages[start + finalI], message);

                    messageModule.sendToPlayer(StorageKey.MESSAGE_MENU_SUCCESS, new Player[]{player}, MessageType.MESSAGE,
                            new Replacement("{message_name}", messages[start + finalI]),
                            new Replacement("{message}", message)
                            );
                }));
                player.closeInventory();
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
