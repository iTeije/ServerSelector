package eu.iteije.serverselector.spigot.commands;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.commands.CommandModule;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.commands.labelcommands.HubMainCommand;
import eu.iteije.serverselector.spigot.commands.labelcommands.SpigotMainCommand;
import eu.iteije.serverselector.spigot.commands.subcommands.AdminSubCommand;
import eu.iteije.serverselector.spigot.commands.subcommands.ConsoleSubCommand;
import eu.iteije.serverselector.spigot.commands.subcommands.HelpSubCommand;
import eu.iteije.serverselector.spigot.commands.subcommands.LeaveQueueSubCommand;

public class SpigotCommandModule {

    public SpigotCommandModule(ServerSelectorSpigot serverSelectorSpigot) {
        SpigotMainCommand spigotMainCommand = new SpigotMainCommand(serverSelectorSpigot);
        serverSelectorSpigot.getCommand("serverselector").setExecutor(spigotMainCommand);
        serverSelectorSpigot.getCommand("serverselector").setTabCompleter(spigotMainCommand);

        HubMainCommand hubMainCommand = new HubMainCommand(serverSelectorSpigot, serverSelectorSpigot.getSelectorModule().getActionManager());
        serverSelectorSpigot.getCommand("hub").setExecutor(hubMainCommand);

        CommandModule commandModule = ServerSelector.getInstance().getCommandModule();

        commandModule.registerSubCommands(
                new HelpSubCommand(serverSelectorSpigot),
                new AdminSubCommand(serverSelectorSpigot),
                new ConsoleSubCommand(serverSelectorSpigot),
                new LeaveQueueSubCommand(serverSelectorSpigot)
        );
    }
}
