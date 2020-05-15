package eu.iteije.serverselector.common.commands.objects;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.platform.Platform;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SubCommand {

    @Getter private String command;
    @Getter private String syntax;
    @Getter private List<Argument> arguments = new ArrayList<>();

    public SubCommand(String commandArgument, String syntax) {
        this.command = commandArgument;
        this.syntax = syntax;

        // Check whether the server is running spigot or not
        if (ServerSelector.getInstance().getPlatform() == Platform.SPIGOT) {
            // Generate permission
            Bukkit.getPluginManager().addPermission(new Permission("serverselector.commands." + commandArgument));
        }
    }

    /**
     * @param executor command sender
     * @return true if the player has the correct permission
     */
    public boolean hasPermission(CommonExecutor executor) {
        return executor.hasPermission("serverselector.commands." + command)
                || executor.hasPermission("serverselector.commands.*")
                || executor.hasPermission("serverselector.*");
    }

    /**
     * Register all args
     * @param args all given arguments
     */
    protected void registerArguments(Argument... args) {
        arguments.addAll(Arrays.asList(args));
    }

    /**
     * @param executor the one who executed the command
     * @param args all given arguments
     */
    public abstract void onExecute(CommonExecutor executor, String[] args, String label);

}
