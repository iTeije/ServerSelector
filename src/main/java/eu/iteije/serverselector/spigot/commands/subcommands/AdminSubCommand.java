package eu.iteije.serverselector.spigot.commands.subcommands;

import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.Argument;
import eu.iteije.serverselector.common.commands.objects.SubCommand;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.commands.subcommands.arguments.admin.AdminMessageArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class AdminSubCommand extends SubCommand {

    private ServerSelectorSpigot serverSelectorSpigot;

    public AdminSubCommand(ServerSelectorSpigot serverSelectorSpigot) {
        super("admin", "Shows a page with all admin commands");
        registerArguments(
                new Argument("messages", "Modify messages across network", new AdminMessageArgument(this, "messages", serverSelectorSpigot))
        );
        this.serverSelectorSpigot = serverSelectorSpigot;
    }

    @Override
    public void onExecute(CommonExecutor executor, String[] args, String label) {
        CommandSender sender = executor.getSender();
        if (args.length == 0) {
            Bukkit.getServer().dispatchCommand(sender, "ss help admin");
            return;
        }

        for (Argument argument : this.getArguments()) {
            if (args[0].equals(argument.getSyntax())) {
                if (argument.getArgumentHandler().hasPermission(executor)) {
                    argument.getArgumentHandler().onExecute(executor, args);
                }
            }
        }


    }
}
