package eu.iteije.serverselector.spigot.menus;

import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.services.menus.menu.Menu;

public class MainSelectorMenu extends Menu {

    private ServerSelectorSpigot serverSelectorSpigot;

    public MainSelectorMenu(ServerSelectorSpigot instance) {
        super(instance, instance.getMessageModule().getMessage(StorageKey.MAIN_MENU_TITLE), 54);
        this.serverSelectorSpigot = instance;
    }

}
