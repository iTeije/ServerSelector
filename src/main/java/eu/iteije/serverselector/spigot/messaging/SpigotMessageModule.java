package eu.iteije.serverselector.spigot.messaging;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.messaging.MessageModule;
import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SpigotMessageModule {

    private MessageModule messageModule;

    public SpigotMessageModule() {
        this.messageModule = ServerSelector.getInstance().getMessageModule();
    }

    // All local server methods
    public void sendString(String message, Player[] players, MessageType messageType, Replacement... replacements) {
        messageType.send(messageModule.convert(message, true, replacements), players);
    }

    public void sendToPlayer(StorageKey storageKey, Player[] players, MessageType messageType, Replacement... replacements) {
        messageType.send(messageModule.convert(storageKey, true, replacements), players);
    }

    public void sendString(String message, CommandSender sender, MessageType messageType, Replacement... replacements) {
        if (sender instanceof Player) {
            sendString(message, new Player[]{(Player) sender}, messageType, replacements);
        } else if (sender instanceof ConsoleCommandSender) {
            Bukkit.getLogger().info(messageModule.convert(message, true, replacements));
        }
    }

    public void send(StorageKey storageKey, CommandSender sender, MessageType messageType, Replacement... replacements) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sendToPlayer(storageKey, new Player[]{player}, messageType, replacements);
        } else if (sender instanceof ConsoleCommandSender) {
            Bukkit.getConsoleSender().sendMessage(messageModule.convert(storageKey, true, replacements));
        }
    }

    public void localBroadcast(StorageKey storageKey, MessageType messageType, Replacement... replacements) {
        messageType.send(messageModule.convert(storageKey, true, replacements));
    }

    // Server wide (bungee) methods
    public void sendToBungeePlayer(String message, Object[] players, MessageType messageType, ServerSelectorSpigot serverSelectorSpigot, Replacement... replacements) {
        messageType.sendBungee(messageModule.convert(message, false, replacements), MessageChannel.BUNGEE_GLOBAL.getChannel(), serverSelectorSpigot,
                convertPlayerArray(players));
    }

    public void sendToBungeePlayer(StorageKey storageKey, Object[] players, MessageType messageType, ServerSelectorSpigot serverSelectorSpigot, Replacement... replacements) {
        messageType.sendBungee(messageModule.convert(storageKey, false, replacements), MessageChannel.BUNGEE_GLOBAL.getChannel(), serverSelectorSpigot,
                convertPlayerArray(players));
    }

    public void globalBroadcast(StorageKey storageKey, MessageType messageType, ServerSelectorSpigot serverSelectorSpigot, Replacement... replacements) {
        messageType.sendBungee(messageModule.convert(storageKey, false, replacements), MessageChannel.BUNGEE_GLOBAL.getChannel(), serverSelectorSpigot, null);
    }

    public void globalBroadcast(String message, MessageType messageType, ServerSelectorSpigot serverSelectorSpigot, Replacement... replacements) {
        messageType.sendBungee(messageModule.convert(message, false, replacements), MessageChannel.BUNGEE_GLOBAL.getChannel(), serverSelectorSpigot, null);
    }




    public String getMessage(StorageKey storageKey) {
        return SpigotFileModule.getFile(storageKey).getString(storageKey);
    }

    private String[] convertPlayerArray(Object[] players) {
        if (players instanceof String[]) return (String[]) players;
        if (players instanceof Player[]) {
            Player[] playerArray = (Player[]) players.clone();
            ArrayList<String> playerNames = new ArrayList<>();
            for (Player player : playerArray) {
                playerNames.add(player.getName());
            }
            return playerNames.toArray(new String[playerNames.size()]);
        }
        return null;
    }

}
