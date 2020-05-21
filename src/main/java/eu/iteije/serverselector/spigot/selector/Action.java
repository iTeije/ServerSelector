package eu.iteije.serverselector.spigot.selector;

import org.bukkit.entity.Player;

public interface Action {

    void execute(String context, Player player);

}
