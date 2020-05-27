package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.entity.Player;

public class ConsoleCommandAction extends Action {

    private ServerSelectorSpigot instance;

    public ConsoleCommandAction(ServerSelectorSpigot instance) {
        super(ActionTag.OTHERS);
        this.instance = instance;
    }

    @Override
    public void execute(String context, Player player) {
        context = context.replace("{player}", player.getName());

        instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), context);

        player.closeInventory();
    }
}
