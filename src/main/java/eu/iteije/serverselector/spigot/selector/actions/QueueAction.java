package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.entity.Player;

public class QueueAction extends Action {

    private ServerSelectorSpigot instance;

    public QueueAction(ServerSelectorSpigot instance) {
        super(ActionTag.BUNGEE);
        this.instance = instance;
    }

    @Override
    public void execute(String context, Player player) {
        // We won't split the context into a array since it's easier to do that on bungee level

        // Queue for the given server(s)
        instance.getCommunicationModule().queuePlayer(context, player);
    }
}
