package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.menus.MenuModule;
import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MenuAction extends Action {

    private ServerSelectorSpigot instance;

    public MenuAction(ServerSelectorSpigot instance) {
        super(ActionTag.PLAYER);
        this.instance = instance;
    }

    @Override
    public void execute(String context, Player player) {
        // Open a given menu
        // Load menu module
        MenuModule menuModule = instance.getMenuModule();
        // Try opening the given menu
        try {
            menuModule.getCachedMenu(context).open(player);
        } catch (Exception exception) {
            instance.getMessageModule().send(StorageKey.ACTION_MENU_FAILED, Bukkit.getConsoleSender(), MessageType.MESSAGE,
                    new Replacement("{menu}", context)
                    );
            instance.getMessageModule().sendToPlayer(StorageKey.ACTION_MENU_FAILED, new Player[]{player}, MessageType.MESSAGE,
                    new Replacement("{menu}", context)
                    );
        }
    }
}
