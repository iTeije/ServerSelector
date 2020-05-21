package eu.iteije.serverselector.spigot.commands.subcommands.arguments;

import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.SubCommand;

public abstract class ArgumentHandler {

    private SubCommand subCommand;
    private String argument;

    public ArgumentHandler(SubCommand subCommand, String argument) {
        this.subCommand = subCommand;
        this.argument = argument;
    }

    /**
     * @param executor the one who executed the command
     * @param args all given arguments
     */
    public abstract void onExecute(CommonExecutor executor, String[] args);

    /**
     * @param executor command sender
     * @return true if the player has the correct permission
     */
    public boolean hasPermission(CommonExecutor executor) {
        return executor.hasPermission("serverselector.commands." + subCommand.getCommand() + "." + argument)
                || executor.hasPermission("serverselector.commands." + subCommand.getCommand() + ".*")
                || executor.hasPermission("serverselector.commands.*")
                || executor.hasPermission("serverselector.*");
    }

}
