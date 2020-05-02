package eu.iteije.serverselector.spigot.commands.labelcommands;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.commands.CommandModule;
import eu.iteije.serverselector.common.commands.adapters.SpigotCommandSenderAdapter;
import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.Argument;
import eu.iteije.serverselector.common.commands.objects.SubCommand;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.enums.ReplacementType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpigotMainCommand implements CommandExecutor, TabCompleter {

    private ServerSelectorSpigot serverSelectorSpigot;
    private CommandModule commandModule;

    public SpigotMainCommand(ServerSelectorSpigot serverSelectorSpigot) {
        this.serverSelectorSpigot = serverSelectorSpigot;
        commandModule = ServerSelector.getInstance().getCommandModule();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommonExecutor executor = new SpigotCommandSenderAdapter(sender);
        Player player = (Player) sender;

        SpigotMessageModule messageModule = serverSelectorSpigot.getMessageModule();

        // Send plugin credits if args are not given
        if (args.length == 0) {
            messageModule.sendString("&3ServerSelector version {version} &3by {author}&3, use {help_command} &3for help.", new Player[]{player}, MessageType.SPIGOT_MESSAGE,
                    new Replacement("{version}", serverSelectorSpigot.getDescription().getVersion(), ReplacementType.VARIABLE),
                    new Replacement("{author}", "iTeije", ReplacementType.VARIABLE),
                    new Replacement("{help_command}", "/ss help", ReplacementType.COMMAND));
            return true;
        }

        SubCommand subCommand = commandModule.getSubCommand(args[0].toLowerCase());

        if (subCommand != null) {
            if (subCommand.hasPermission(executor)) {
                String[] subArgs = new String[args.length - 1];

                if (args.length != 1) System.arraycopy(args, 1, subArgs, 0, args.length -1);

                // Try executing the command
                try {
                    subCommand.onExecute(executor, subArgs, label);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    messageModule.sendToPlayer(StorageKey.COMMAND_ERROR, new Player[]{player}, MessageType.SPIGOT_MESSAGE);
                }
                return true;
            } else {
                messageModule.sendToPlayer(StorageKey.PERMISSION_ERROR, new Player[]{player}, MessageType.SPIGOT_MESSAGE);
                return true;
            }
        } else {
            String fullCommand = commandModule.getFullCommand(label, args);
            messageModule.sendToPlayer(StorageKey.COMMAND_NOT_FOUND, new Player[]{player}, MessageType.SPIGOT_MESSAGE,
                    new Replacement("{command}", fullCommand, ReplacementType.COMMAND_ERROR),
                    new Replacement("{command_suggestion}", "/ss help", ReplacementType.COMMAND_ERROR)
                    );
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        for (String subCommand : commandModule.getSubCommands()) {
            if (args.length <= 1 && subCommand.startsWith(args[0])) completions.add(subCommand);
        }

        if (args.length == 2) {
            SubCommand subCommand = commandModule.getSubCommand(args[0].toLowerCase());
            if (subCommand == null) return new ArrayList<>();
            for (Argument argument : subCommand.getArguments()) {
                if (argument.getSyntax().startsWith(args[1].toLowerCase())) {
                    completions.add(argument.getSyntax());
                }
            }
        }
        return completions;
    }
}
