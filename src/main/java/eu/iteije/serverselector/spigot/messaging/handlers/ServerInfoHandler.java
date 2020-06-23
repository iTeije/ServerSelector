package eu.iteije.serverselector.spigot.messaging.handlers;

import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.interfaces.SpigotHandlerImplementation;
import eu.iteije.serverselector.spigot.selector.SelectorModule;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerInfoHandler implements SpigotHandlerImplementation {

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
                    input.readLong(),
                    input.readInt(),
                    input.readInt(),
                    input.readUTF().split(","),
                    input.readUTF(),
                    input.readUTF(),
                    input.readUTF(),
                    input.readLong(),
                    input.readInt(),
                    input.readLong(),
                    input.readLong()
            );

            selectorModule.getStatusUpdater().updateServerInfo(data);
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in ServerInfoHandler.", exception);
            exception.printStackTrace();
        }

    }
}
