package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.enums.ReplacementType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.menus.MenuModule;
import eu.iteije.serverselector.spigot.selector.Action;
import org.bukkit.entity.Player;

public class MenuAction implements Action {

    private ServerSelectorSpigot instance;

    public MenuAction(ServerSelectorSpigot instance) {
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
            exception.printStackTrace();
            instance.getMessageModule().sendToPlayer(StorageKey.MENU_ACTION_MENU_FAILED, new Player[]{player}, MessageType.MESSAGE,
                    new Replacement("{menu}", context, ReplacementType.VARIABLE_ERROR)
                    );
        }
    }
}
