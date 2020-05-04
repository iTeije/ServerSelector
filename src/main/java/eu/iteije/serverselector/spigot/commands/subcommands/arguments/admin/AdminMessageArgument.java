package eu.iteije.serverselector.spigot.commands.subcommands.arguments.admin;

import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.SubCommand;
import eu.iteije.serverselector.common.messaging.enums.Color;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.enums.ReplacementType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.commands.subcommands.arguments.ArgumentHandler;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
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
        Player player = (Player) executor.getSender();

        spigotMessageModule.sendToPlayer(StorageKey.MENU_OPENING, new Player[]{player}, MessageType.MESSAGE,
                new Replacement("{menu}", spigotMessageModule.getMessage(StorageKey.MESSAGE_MENU_NAME), ReplacementType.VARIABLE)
        );

        spigotMessageModule.globalBroadcast(Color.DARK_AQUA.getChatCode() + player.getName() + " is opening a menu, how cool", MessageType.MESSAGE, serverSelectorSpigot);
        spigotMessageModule.globalBroadcast("/say hi", MessageType.MESSAGE, serverSelectorSpigot);
        spigotMessageModule.sendToBungeePlayer("This message is dedicated to iteije", new String[]{"iTeije"}, MessageType.MESSAGE, serverSelectorSpigot);
        spigotMessageModule.sendToBungeePlayer("This message is dedicated to iromke", new String[]{"iRomke"}, MessageType.MESSAGE, serverSelectorSpigot);
        spigotMessageModule.sendToBungeePlayer("This message is dedicated to both", new String[]{"iTeije", "iRomke"}, MessageType.MESSAGE, serverSelectorSpigot);
    }

}
