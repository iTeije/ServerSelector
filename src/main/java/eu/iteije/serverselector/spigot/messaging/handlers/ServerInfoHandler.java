package eu.iteije.serverselector.spigot.messaging.handlers;

import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.interfaces.SpigotCommunicationImplementation;
import eu.iteije.serverselector.spigot.selector.SelectorModule;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerInfoHandler implements SpigotCommunicationImplementation {

    private ServerSelectorSpigot instance;

    public ServerInfoHandler(ServerSelectorSpigot instance) {
        this.instance = instance;
    }

    @Override
    public void process(DataInputStream input) {
        try {
            SelectorModule selectorModule = instance.getSelectorModule();

            ServerData data = new ServerData(
                    input.readUTF(),
                    input.readUTF(),
                    input.readUTF(),
                    input.readUTF(),
                    input.readLong()
            );

            selectorModule.getMenuUpdater().updateServerInfo(data);
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in ServerInfoHandler.", exception);
        }

    }
}
