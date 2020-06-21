package eu.iteije.serverselector.bungee.messaging;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.messaging.handlers.*;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeHandlerImplementation;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.MessageModule;
import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class BungeeCommunicationModule implements Listener {

    private ServerSelectorBungee instance;
    private MessageModule messageModule;

    private HashMap<String, BungeeHandlerImplementation> bungeeHandlers = new HashMap<>();

    public BungeeCommunicationModule(ServerSelectorBungee instance) {
        this.instance = instance;

        instance.getProxy().registerChannel(MessageChannel.BUNGEE_GLOBAL.getChannel());
        saveHandlers();

        this.messageModule = ServerSelector.getInstance().getMessageModule();
    }

    public BungeeHandlerImplementation getHandler(String name) {
        return bungeeHandlers.get(name);
    }

    public void addHandler(String name, BungeeHandlerImplementation implementation) {
        bungeeHandlers.put(name, implementation);
    }

    public void saveHandlers() {
        addHandler("Broadcast", new BroadcastHandler(instance));
        addHandler("SendPlayer", new SendPlayerHandler(instance));
        addHandler("ServerInfoRequest", new ServerInfoRequestHandler(instance));
        addHandler("QueuePlayer", new QueuePlayerHandler(instance));
        addHandler("MessagePlayer", new MessagePlayerHandler());
        addHandler("LeaveQueue", new LeaveQueueHandler(instance));
        addHandler("UpdateMessage", new UpdateMessageHandler(instance));
    }

    public void sendMessage(StorageKey key, ProxiedPlayer player, String sender, Replacement... replacements) {
        String[] messagePlayerRequest = {messageModule.convert(key, true, replacements), player.getName()};
        getHandler("MessagePlayer").process(messageModule.getDataInputStream(messagePlayerRequest), sender);
    }

    public void sendPlayer(UUID uuid, String server) {
        ProxiedPlayer player = instance.getProxy().getPlayer(uuid);
        String[] sendPlayerRequest = {server, player.getName()};
        getHandler("SendPlayer").process(messageModule.getDataInputStream(sendPlayerRequest), player.getServer().getInfo().getName());
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals(MessageChannel.BUNGEE_GLOBAL.getChannel())) return;
        if (!(event.getSender() instanceof Server)) return;

        ByteArrayInputStream stream = new ByteArrayInputStream(event.getData());
        DataInputStream inputStream = new DataInputStream(stream);
        try {
            String type = inputStream.readUTF();

            BungeeHandlerImplementation implementation = getHandler(type);

            if (implementation != null)
                implementation.process(inputStream, ((Server) event.getSender()).getInfo().getName());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
