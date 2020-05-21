package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.selector.Action;
import org.bukkit.entity.Player;

public class SendAction implements Action {

    private ServerSelectorSpigot instance;

    public SendAction(ServerSelectorSpigot instance) {
        this.instance = instance;
    }

    @Override
    public void execute(String context, Player player) {
        // Queue for a given server
        // TODO: queue
        player.sendMessage("You're sent");
    }
}
