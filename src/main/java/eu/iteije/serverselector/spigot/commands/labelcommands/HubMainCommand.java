package eu.iteije.serverselector.spigot.commands.labelcommands;

import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import eu.iteije.serverselector.spigot.selector.ActionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubMainCommand implements CommandExecutor {

    private final ServerSelectorSpigot instance;
    private final ActionManager actionManager;
    private final String lobbyServer;

    public HubMainCommand(ServerSelectorSpigot serverSelectorSpigot, ActionManager actionManager) {
        this.instance = serverSelectorSpigot;
        this.actionManager = actionManager;
        this.lobbyServer = SpigotFileModule.getFile(StorageKey.LOBBY_SERVER).getString(StorageKey.LOBBY_SERVER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        SpigotMessageModule messageModule = instance.getMessageModule();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("serverselector.commands.hub")) {
                if (lobbyServer == null) {
                    messageModule.send(StorageKey.COMMAND_ERROR, sender, MessageType.MESSAGE);
                    return true;
                }
                actionManager.getActionByName("QUEUE").execute(this.lobbyServer, player);
            } else {
                messageModule.send(StorageKey.PERMISSION_ERROR, sender, MessageType.MESSAGE);
            }
        } else {
            messageModule.send(StorageKey.COMMAND_PLAYER_ONLY, sender, MessageType.MESSAGE);
        }

        return true;
    }

}
