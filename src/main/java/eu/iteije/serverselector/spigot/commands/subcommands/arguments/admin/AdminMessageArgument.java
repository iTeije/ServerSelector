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

    private ServerSelectorSpigot instance;

    public AdminMessageArgument(SubCommand subCommand, String argument, ServerSelectorSpigot instance) {
        super(subCommand, argument);
        this.instance = instance;
    }

    @Override
    public void onExecute(CommonExecutor executor, String[] args) {
        SpigotMessageModule messageModule = instance.getMessageModule();

        CommandSender sender = executor.getSender();

        if (sender instanceof Player) {
            Player player = (Player) executor.getSender();
            messageModule.sendToPlayer(StorageKey.MENU_OPENING, new Player[]{player}, MessageType.MESSAGE,
                    new Replacement("{menu}", messageModule.getMessage(StorageKey.MESSAGE_MENU_NAME))
            );

            AdminMessagesMenu messagesMenu = new AdminMessagesMenu(instance, 1);
            messagesMenu.open(player);
        } else {
            messageModule.send(StorageKey.COMMAND_PLAYER_ONLY, sender, MessageType.MESSAGE);
        }
    }

}
