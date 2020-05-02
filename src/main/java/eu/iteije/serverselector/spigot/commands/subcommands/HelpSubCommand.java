package eu.iteije.serverselector.spigot.commands.subcommands;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.commands.CommandModule;
import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.Argument;
import eu.iteije.serverselector.common.commands.objects.SubCommand;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.enums.ReplacementType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import org.bukkit.entity.Player;

public class HelpSubCommand extends SubCommand {

    private CommandModule commandModule;
    private ServerSelectorSpigot serverSelectorSpigot;

    public HelpSubCommand(ServerSelectorSpigot serverSelectorSpigot) {
        super("help", "Shows a page with available commands");
        this.commandModule = ServerSelector.getInstance().getCommandModule();
        this.serverSelectorSpigot = serverSelectorSpigot;
    }

    @Override
    public void onExecute(CommonExecutor sender, String[] args, String label) {
        SpigotMessageModule spigotMessageModule = serverSelectorSpigot.getMessageModule();

        Player player = (Player) sender.getSender();
        if (args.length == 1) {
            args[0] = args[0].toLowerCase();
            SubCommand subCommand = commandModule.getSubCommand(args[0]);
            if (subCommand != null) {
                if (!subCommand.hasPermission(sender)) {
                    spigotMessageModule.sendToPlayer(StorageKey.PERMISSION_ERROR, new Player[]{player}, MessageType.MESSAGE);
                    return;
                }
                spigotMessageModule.sendToPlayer(StorageKey.HELP_COMMAND_DEDICATED, new Player[]{player}, MessageType.MESSAGE,
                        new Replacement("{command}", commandModule.getFullCommand(label, args), ReplacementType.COMMAND)
                        );
                if (subCommand.getArguments().size() == 0) {
                    spigotMessageModule.sendToPlayer(StorageKey.HELP_COMMAND_NO_RESULTS, new Player[]{player}, MessageType.MESSAGE);
                    return;
                }
                for (Argument argument : subCommand.getArguments()) {
                    String subCmd = subCommand.getCommand();
                    if (subCommand.getSyntax() != null) subCmd = subCmd + " " + argument.getSyntax();
                    spigotMessageModule.sendToPlayer(StorageKey.HELP_COMMAND_ITEM, new Player[]{player}, MessageType.MESSAGE,
                            new Replacement("{command}", label, ReplacementType.COMMAND),
                            new Replacement("{subcommand}", subCmd, ReplacementType.COMMAND),
                            new Replacement("{description}", argument.getDescription(), ReplacementType.COMMAND_DESCRIPTION)
                    );
                }
                return;
            }
        }

        spigotMessageModule.sendToPlayer(StorageKey.HELP_COMMAND_TITLE, new Player[]{player}, MessageType.MESSAGE);

        if (commandModule.getSubCommandHandlers().size() == 0) {
            spigotMessageModule.sendToPlayer(StorageKey.HELP_COMMAND_NO_RESULTS, new Player[]{player}, MessageType.MESSAGE);
            return;
        }
        for (SubCommand subCommand : commandModule.getSubCommandHandlers()) {
            spigotMessageModule.sendToPlayer(StorageKey.HELP_COMMAND_ITEM, new Player[]{player}, MessageType.MESSAGE,
                    new Replacement("{command}", label, ReplacementType.COMMAND),
                    new Replacement("{subcommand}", subCommand.getCommand(), ReplacementType.COMMAND),
                    new Replacement("{description}", subCommand.getSyntax(), ReplacementType.COMMAND_DESCRIPTION)
            );
        }


    }


}
