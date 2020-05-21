package eu.iteije.serverselector.common.commands;

import eu.iteije.serverselector.common.commands.objects.SubCommand;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandModule {

    private Map<String, SubCommand> subCommands = new HashMap<>();
    @Getter
    private List<String> aliases = new ArrayList<>();

    public CommandModule() {
        // Register all sub commands

    }

    /**
     * @return all registered sub commands as String
     */
    public List<String> getSubCommands() {
        return new ArrayList<>(subCommands.keySet());
    }

    /**
     * @return all registered sub command handlers
     */
    public List<SubCommand> getSubCommandHandlers() {
        return new ArrayList<>(subCommands.values());
    }

    /**
     * @param subCommand subcommand that has to be registered
     */
    public void registerSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getCommand(), subCommand);
    }

    public void registerSubCommands(SubCommand... subCommands) {
        for (SubCommand subCommand : subCommands) {
            this.subCommands.put(subCommand.getCommand(), subCommand);
        }
    }

    /**
     * @param argument get command from name
     * @return sub command handler
     */
    public SubCommand getSubCommand(String argument) {
        return subCommands.get(argument);
    }


    /**
     * Some weird string concatenation
     * @param label main command
     * @param args all arguments
     * @return generated command
     */
    public String getFullCommand(String label, String[] args) {
        String fullCommand = "/" + label;
        for (String argument : args) {
            fullCommand += " " + argument;
        }
        return fullCommand;
    }

}
