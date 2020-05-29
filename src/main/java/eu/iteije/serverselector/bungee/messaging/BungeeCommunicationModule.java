package eu.iteije.serverselector.bungee.messaging;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.messaging.handlers.BroadcastHandler;
import eu.iteije.serverselector.bungee.messaging.handlers.QueuePlayerHandler;
import eu.iteije.serverselector.bungee.messaging.handlers.SendPlayerHandler;
import eu.iteije.serverselector.bungee.messaging.handlers.ServerInfoRequestHandler;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeCommunicationImplementation;
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

public class BungeeCommunicationModule implements Listener {

    private ServerSelectorBungee serverSelectorBungee;

    private HashMap<String, BungeeCommunicationImplementation> bungeeHandlers = new HashMap<>();

    public BungeeCommunicationModule(ServerSelectorBungee serverSelectorBungee) {
        this.serverSelectorBungee = serverSelectorBungee;

        serverSelectorBungee.getProxy().registerChannel(MessageChannel.BUNGEE_GLOBAL.getChannel());
        saveHandlers();
    }

    public BungeeCommunicationImplementation getHandler(String name) {
        return bungeeHandlers.get(name);
    }

    public void addHandler(String name, BungeeCommunicationImplementation implementation) {
        bungeeHandlers.put(name, implementation);
    }

    public void saveHandlers() {
        addHandler("Broadcast", new BroadcastHandler(serverSelectorBungee));
        addHandler("SendPlayer", new SendPlayerHandler(serverSelectorBungee));
        addHandler("ServerInfoRequest", new ServerInfoRequestHandler(serverSelectorBungee));
        addHandler("QueuePlayer", new QueuePlayerHandler(serverSelectorBungee));
    }

    public void sendMessage(StorageKey key, ProxiedPlayer player, MessageModule messageModule, String sender, Replacement... replacements) {
        String[] messagePlayerRequest = {messageModule.convert(key, true, replacements), player.getName()};
        getHandler("MessagePlayer").process(messageModule.getDataInputStream(messagePlayerRequest), sender);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals(MessageChannel.BUNGEE_GLOBAL.getChannel())) return;
        if (!(event.getSender() instanceof Server)) return;

        ByteArrayInputStream stream = new ByteArrayInputStream(event.getData());
        DataInputStream inputStream = new DataInputStream(stream);
        try {
            String type = inputStream.readUTF();

            BungeeCommunicationImplementation implementation = getHandler(type);
            if (implementation != null) implementation.process(inputStream, ((Server) event.getSender()).getInfo().getName());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
