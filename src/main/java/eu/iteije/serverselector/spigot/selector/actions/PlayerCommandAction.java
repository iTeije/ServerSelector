package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.entity.Player;

public class PlayerCommandAction extends Action {

    public PlayerCommandAction() {
        super(ActionTag.PLAYER);
    }

    @Override
    public void execute(String context, Player player) {
        context = context.replace("{player}", player.getName());

        player.performCommand(context);

        player.closeInventory();
    }
}
