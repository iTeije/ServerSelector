package eu.iteije.serverselector.common.commands.adapters;

import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class SpigotCommandSenderAdapter implements CommonExecutor {

    private CommandSender sender;

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public CommandSender getSender() {
        return sender;
    }

}
