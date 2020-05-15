package eu.iteije.serverselector.spigot.services.actionqueue;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.players.ServerSelectorPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class Action {

    @Getter private BiConsumer<Action, String> onExecute = (a, b) -> {};
    @Getter private ActionType actionType;
    private ServerSelectorSpigot instance;
    @Getter private Player player;

    public Action(ServerSelectorSpigot instance, Player player, ActionType type) {
        ServerSelectorPlayer pluginPlayer = instance.getPlayerModule().getPlayer(player.getUniqueId());
        this.actionType = type;
        this.player = player;
        this.instance = instance;

        pluginPlayer.queueAction(this);
    }

    public Action onExecute(BiConsumer<Action, String> onExecute) {
        this.onExecute = onExecute;
        return this;
    }

    public void execute(String content) {
        this.getOnExecute().accept(this, content);
        instance.getPlayerModule().getPlayer(player.getUniqueId()).removeAction(this.actionType);
    }
}
