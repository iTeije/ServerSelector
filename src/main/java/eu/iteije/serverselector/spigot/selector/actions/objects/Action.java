package eu.iteije.serverselector.spigot.selector.actions.objects;

import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class Action {

    @Getter private ActionTag tag;

    public Action(ActionTag tag) {
        this.tag = tag;
    }

    public abstract void execute(String context, Player player);

    public boolean hasTag(ActionTag tag) {
        return this.tag == tag;
    }

}
