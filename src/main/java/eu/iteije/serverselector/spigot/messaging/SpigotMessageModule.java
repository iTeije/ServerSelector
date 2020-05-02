package eu.iteije.serverselector.spigot.messaging;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.messaging.MessageModule;
import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpigotMessageModule {

    private MessageModule messageModule;

    public SpigotMessageModule() {
        this.messageModule = ServerSelector.getInstance().getMessageModule();
    }

    // All local server methods
    public void sendString(String message, Player[] players, MessageType messageType, Replacement... replacements) {
        messageType.send(messageModule.convert(message, replacements), players);
    }

    public void sendToPlayer(StorageKey storageKey, Player[] players, MessageType messageType, Replacement... replacements) {
        messageType.send(messageModule.convert(storageKey, replacements), players);
    }

    public void localBroadcast(StorageKey storageKey, MessageType messageType, Replacement... replacements) {
        messageType.send(messageModule.convert(storageKey, replacements));
    }

    // Server wide (bungee) methods
    public void sendBungeeString(String message, Player[] players, MessageType messageType, ServerSelectorSpigot serverSelectorSpigot, Replacement... replacements) {
        messageType.sendBungee(messageModule.convert(message, replacements), MessageChannel.BUNGEE_GLOBAL.getChannel(), serverSelectorSpigot, players);
    }

    public void sendToBungeePlayer(StorageKey storageKey, Player[] players, MessageType messageType, ServerSelectorSpigot serverSelectorSpigot, Replacement... replacements) {
        messageType.sendBungee(messageModule.convert(storageKey, replacements), MessageChannel.BUNGEE_GLOBAL.getChannel(), serverSelectorSpigot, players);
    }

    public void globalBroadcast(StorageKey storageKey, MessageType messageType, ServerSelectorSpigot serverSelectorSpigot, Replacement... replacements) {
        messageType.sendBungee(messageModule.convert(storageKey, replacements), MessageChannel.BUNGEE_GLOBAL.getChannel(), serverSelectorSpigot);
    }





    public String getMessage(StorageKey storageKey) {
        return SpigotFileModule.getFile(storageKey).getString(storageKey);
    }

}
