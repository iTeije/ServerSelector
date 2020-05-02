package eu.iteije.serverselector.spigot.commands.subcommands;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.commands.CommandModule;
import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.Argument;
import eu.iteije.serverselector.common.commands.objects.SubCommand;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.commands.subcommands.arguments.admin.AdminMessageArgument;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import org.bukkit.entity.Player;

public class AdminSubCommand extends SubCommand {

    private CommandModule commandModule;
    private ServerSelectorSpigot serverSelectorSpigot;

    public AdminSubCommand(ServerSelectorSpigot serverSelectorSpigot) {
        super("admin", "Shows a page with all admin commands");
        registerArguments(
                new Argument("messages", "Modify messages across network", new AdminMessageArgument(this, "messages", serverSelectorSpigot))
        );
        this.serverSelectorSpigot = serverSelectorSpigot;
        this.commandModule = ServerSelector.getInstance().getCommandModule();
    }

    @Override
    public void onExecute(CommonExecutor sender, String[] args, String label) {
        SpigotMessageModule spigotMessageModule = serverSelectorSpigot.getMessageModule();

        Player player = (Player) sender.getSender();

        if (args.length == 0) {
            player.performCommand("ss help admin");
            return;
        }

        for (Argument argument : this.getArguments()) {
            if (args[0].equals(argument.getSyntax())) {
                if (argument.getArgumentHandler().hasPermission(sender)) {
                    argument.getArgumentHandler().onExecute(sender, args);
                }
            }
        }


    }
}
