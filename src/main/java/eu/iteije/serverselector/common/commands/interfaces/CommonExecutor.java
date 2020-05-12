package eu.iteije.serverselector.common.commands.interfaces;

import org.bukkit.command.CommandSender;

public interface CommonExecutor {

    boolean hasPermission(String permission);
    CommandSender getSender();

}
