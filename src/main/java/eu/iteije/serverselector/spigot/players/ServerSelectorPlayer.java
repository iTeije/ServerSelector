package eu.iteije.serverselector.spigot.players;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.services.actionqueue.Action;
import eu.iteije.serverselector.spigot.services.actionqueue.ActionType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerSelectorPlayer {

    private ServerSelectorSpigot serverSelectorSpigot;

    private final UUID uuid;

    private List<Action> queue = new ArrayList<>();

    @Getter @Setter private boolean toggledPlayers;

    public ServerSelectorPlayer(ServerSelectorSpigot serverSelectorSpigot, UUID uuid) {
        this.serverSelectorSpigot = serverSelectorSpigot;
        this.uuid = uuid;
        this.toggledPlayers = false;
    }

    // Queue a action
    public void queueAction(Action action) {
        queue.add(action);
    }

    // Check whether the player has an action of a given type
    public boolean hasAction(ActionType type) {
        for (Action action : queue) {
            if (action.getActionType() == type) return true;
        }
        return false;
    }

    // Get a queued action of a given type
    public Action getAction(ActionType type) {
        for (Action action : queue) {
            if (action.getActionType() == type) return action;
        }
        return null;
    }

    // Remove a queued action
    public void removeAction(ActionType type) {
        queue.removeIf(action -> action.getActionType() == type);
    }
    public void removeAction(Action action) {
        queue.remove(action);
    }
    public void clearQueue() {
        queue.clear();
    }

    // Get bukkit player
    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}
