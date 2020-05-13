package eu.iteije.serverselector.spigot.commands.subcommands.arguments.console;

import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.SubCommand;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.commands.subcommands.arguments.ArgumentHandler;
import eu.iteije.serverselector.spigot.files.SpigotFile;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;

public class ConsoleMessageArgument extends ArgumentHandler {

    private ServerSelectorSpigot serverSelectorSpigot;

    public ConsoleMessageArgument(SubCommand subCommand, String argument, ServerSelectorSpigot serverSelectorSpigot) {
        super(subCommand, argument);
        this.serverSelectorSpigot = serverSelectorSpigot;
    }

    @Override
    public void onExecute(CommonExecutor executor, String[] args) {
        SpigotMessageModule spigotMessageModule = serverSelectorSpigot.getMessageModule();

        CommandSender sender = executor.getSender();

        if (sender instanceof ConsoleCommandSender) {
            String messageName = args[1];

            args = Arrays.copyOfRange(args, 2, args.length);

            String message = String.join(" ", args);

            SpigotFile messagesFile = SpigotFileModule.getFileByName("messages.yml");

            messagesFile.setStringToPath(message, messageName);
            messagesFile.save();
        } else {
            spigotMessageModule.send(StorageKey.COMMAND_CONSOLE_ONLY, sender, MessageType.MESSAGE);
        }
    }

}
