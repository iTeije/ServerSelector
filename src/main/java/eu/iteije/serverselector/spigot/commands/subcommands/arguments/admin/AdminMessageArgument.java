package eu.iteije.serverselector.spigot.commands.subcommands.arguments.admin;

import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.SubCommand;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.commands.subcommands.arguments.ArgumentHandler;
import eu.iteije.serverselector.spigot.menus.AdminMessagesMenu;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminMessageArgument extends ArgumentHandler {

    private ServerSelectorSpigot serverSelectorSpigot;

    public AdminMessageArgument(SubCommand subCommand, String argument, ServerSelectorSpigot serverSelectorSpigot) {
        super(subCommand, argument);
        this.serverSelectorSpigot = serverSelectorSpigot;
    }

    @Override
    public void onExecute(CommonExecutor executor, String[] args) {

        SpigotMessageModule spigotMessageModule = serverSelectorSpigot.getMessageModule();

        CommandSender sender = executor.getSender();

        if (sender instanceof Player) {
            Player player = (Player) executor.getSender();
            spigotMessageModule.sendToPlayer(StorageKey.MENU_OPENING, new Player[]{player}, MessageType.MESSAGE,
                    new Replacement("{menu}", spigotMessageModule.getMessage(StorageKey.MESSAGE_MENU_NAME))
            );

            AdminMessagesMenu messagesMenu = new AdminMessagesMenu(serverSelectorSpigot, 1);
            messagesMenu.open(player);
        } else {
            spigotMessageModule.send(StorageKey.COMMAND_PLAYER_ONLY, sender, MessageType.MESSAGE);
        }
    }

}
