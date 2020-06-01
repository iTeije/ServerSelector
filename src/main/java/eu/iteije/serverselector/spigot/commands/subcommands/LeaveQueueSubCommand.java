package eu.iteije.serverselector.spigot.commands.subcommands;

import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.SubCommand;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveQueueSubCommand extends SubCommand {

    private ServerSelectorSpigot instance;

    public LeaveQueueSubCommand(ServerSelectorSpigot instance) {
        super("leavequeue", "Leave the queue");
        this.instance = instance;
    }

    @Override
    public void onExecute(CommonExecutor executor, String[] args, String label) {
        CommandSender sender = executor.getSender();
        if (sender instanceof Player) {
            String uuid = ((Player) sender).getUniqueId().toString();
            instance.getCommunicationModule().getRequest("LeaveQueue").process(uuid);
        } else {
            instance.getMessageModule().send(StorageKey.COMMAND_PLAYER_ONLY, sender, MessageType.MESSAGE);
        }
    }

}
