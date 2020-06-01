package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.entity.Player;

public class MessageAction extends Action {

    private ServerSelectorSpigot instance;

    public MessageAction(ServerSelectorSpigot instance) {
        super(ActionTag.PLAYER);
        this.instance = instance;
    }

    @Override
    public void execute(String context, Player player) {
        SpigotMessageModule messageModule = instance.getMessageModule();

        messageModule.sendString(context, new Player[]{player}, MessageType.MESSAGE,
                new Replacement("{player}", player.getName())
        );
    }
}
