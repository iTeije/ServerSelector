package eu.iteije.serverselector.bungee.messaging.handlers;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.bungee.files.BungeeFile;
import eu.iteije.serverselector.bungee.files.BungeeFileModule;
import eu.iteije.serverselector.bungee.messaging.interfaces.BungeeHandlerImplementation;
import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import net.md_5.bungee.api.ProxyServer;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UpdateMessageHandler implements BungeeHandlerImplementation {

    private ServerSelectorBungee instance;

    public UpdateMessageHandler(ServerSelectorBungee instance) {
        this.instance = instance;
    }

    @Override
    public void process(DataInputStream input, String sender) {
        try {
            // The path of the message
            String path = input.readUTF();
            // The updated message
            String message = input.readUTF();

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(bytes);

            // Write type and data
            output.writeUTF("UpdateMessage");
            output.writeUTF(path);
            output.writeUTF(message);

            // Send the command to all spigot servers, regardless of their status
            ProxyServer.getInstance().getServers().values().forEach(server -> {
                server.sendData(MessageChannel.BUNGEE_GLOBAL.getChannel(), bytes.toByteArray());
            });

            StorageKey key = StorageKey.valueOf(path.toUpperCase());
            BungeeFile bungeeFile = BungeeFileModule.getFile(key);

            bungeeFile.setString(message, key);
            bungeeFile.save();
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in UpdateMessageHandler", exception);
        } catch (IllegalArgumentException exception) {
            ServerSelectorLogger.console("Couldn't find storage key", exception);
        }
    }
}
