package eu.iteije.serverselector.spigot.commands.subcommands;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.commands.CommandModule;
import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.Argument;
import eu.iteije.serverselector.common.commands.objects.SubCommand;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import org.bukkit.command.CommandSender;

public class HelpSubCommand extends SubCommand {

    private CommandModule commandModule;
    private ServerSelectorSpigot serverSelectorSpigot;

    public HelpSubCommand(ServerSelectorSpigot serverSelectorSpigot) {
        super("help", "Shows a page with available commands");
        this.commandModule = ServerSelector.getInstance().getCommandModule();
        this.serverSelectorSpigot = serverSelectorSpigot;
    }

    @Override
    public void onExecute(CommonExecutor executor, String[] args, String label) {
        SpigotMessageModule spigotMessageModule = serverSelectorSpigot.getMessageModule();

        CommandSender sender = executor.getSender();
        if (args.length == 1) {
            args[0] = args[0].toLowerCase();
            SubCommand subCommand = commandModule.getSubCommand(args[0]);
            if (subCommand != null) {
                if (!subCommand.hasPermission(executor)) {
                    spigotMessageModule.send(StorageKey.PERMISSION_ERROR, sender, MessageType.MESSAGE);
                    return;
                }
                spigotMessageModule.send(StorageKey.HELP_COMMAND_DEDICATED, sender, MessageType.MESSAGE,
                        new Replacement("{command}", commandModule.getFullCommand(label, args))
                        );
                if (subCommand.getArguments().size() == 0) {
                    spigotMessageModule.send(StorageKey.HELP_COMMAND_NO_RESULTS, sender, MessageType.MESSAGE);
                    return;
                }
                for (Argument argument : subCommand.getArguments()) {
                    String subCmd = subCommand.getCommand();
                    if (subCommand.getSyntax() != null) subCmd = subCmd + " " + argument.getSyntax();
                    spigotMessageModule.send(StorageKey.HELP_COMMAND_ITEM, sender, MessageType.MESSAGE,
                            new Replacement("{command}", label),
                            new Replacement("{subcommand}", subCmd),
                            new Replacement("{description}", argument.getDescription())
                    );
                }
                return;
            }
        }

        spigotMessageModule.send(StorageKey.HELP_COMMAND_TITLE, sender, MessageType.MESSAGE);

        if (commandModule.getSubCommandHandlers().size() == 0) {
            spigotMessageModule.send(StorageKey.HELP_COMMAND_NO_RESULTS, sender, MessageType.MESSAGE);
            return;
        }
        for (SubCommand subCommand : commandModule.getSubCommandHandlers()) {
            spigotMessageModule.send(StorageKey.HELP_COMMAND_ITEM, sender, MessageType.MESSAGE,
                    new Replacement("{command}", label),
                    new Replacement("{subcommand}", subCommand.getCommand()),
                    new Replacement("{description}", subCommand.getSyntax())
            );
        }


    }


}
