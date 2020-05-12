package eu.iteije.serverselector.spigot.commands.subcommands;

import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.Argument;
import eu.iteije.serverselector.common.commands.objects.SubCommand;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.commands.subcommands.arguments.console.ConsoleMessageArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ConsoleSubCommand extends SubCommand {

    private ServerSelectorSpigot serverSelectorSpigot;

    public ConsoleSubCommand(ServerSelectorSpigot serverSelectorSpigot) {
        super("console", "Shows a page with all console only commands");
        registerArguments(
                new Argument("message", "Modify a message", new ConsoleMessageArgument(this, "message", serverSelectorSpigot))
        );
        this.serverSelectorSpigot = serverSelectorSpigot;
    }

    @Override
    public void onExecute(CommonExecutor executor, String[] args, String label) {
        CommandSender sender = (CommandSender) executor.getSender();
        Bukkit.broadcastMessage("0");
        if (args.length == 0) {
            Bukkit.getServer().dispatchCommand(sender, "ss help console");
            return;
        }

        Bukkit.broadcastMessage("1");
        for (Argument argument : this.getArguments()) {
            Bukkit.broadcastMessage("2");
            if (args[0].equals(argument.getSyntax())) {
                Bukkit.broadcastMessage("3");
                if (argument.getArgumentHandler().hasPermission(executor)) {
                    argument.getArgumentHandler().onExecute(executor, args);
                }
            }
        }

    }
}
