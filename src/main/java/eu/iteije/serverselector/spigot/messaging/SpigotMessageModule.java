package eu.iteije.serverselector.spigot.messaging;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.messaging.MessageModule;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.storage.StorageKey;
import org.bukkit.entity.Player;

public class SpigotMessageModule {

    private MessageModule messageModule;

    public SpigotMessageModule() {
        this.messageModule = ServerSelector.getInstance().getMessageModule();
    }

    public void sendString(String message, Player[] players, MessageType messageType, Replacement... replacements) {
        messageType.send(messageModule.convert(message, replacements), players);
    }

    public void sendToPlayer(StorageKey storageKey, Player[] players, MessageType messageType, Replacement... replacements) {
        messageType.send(messageModule.convert(storageKey, replacements), players);
    }

    public void broadcast(StorageKey storageKey, MessageType messageType, Replacement... replacements) {
        messageType.send(messageModule.convert(storageKey, replacements));
    }

}
