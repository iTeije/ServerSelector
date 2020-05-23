package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.entity.Player;

public class CloseAction extends Action {

    public CloseAction() {
        super(ActionTag.OTHERS);

    }

    @Override
    public void execute(String context, Player player) {
        // Close inventory of the player
        player.closeInventory();
    }
}
