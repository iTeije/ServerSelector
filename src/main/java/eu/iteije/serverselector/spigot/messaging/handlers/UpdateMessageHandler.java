package eu.iteije.serverselector.spigot.messaging.handlers;

import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.spigot.messaging.interfaces.SpigotHandlerImplementation;
import org.bukkit.Bukkit;

import java.io.DataInputStream;
import java.io.IOException;

public class UpdateMessageHandler implements SpigotHandlerImplementation {

    public UpdateMessageHandler() {

    }

    @Override
    public void process(DataInputStream input) {
        try {
            String path = input.readUTF();
            String message = input.readUTF();

            // Make up the command (don't want to rewrite everything and want the console to update a message manually at any given time)
            String command = "ss console message " + path + " " + message;

            // Dispatch the command
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        } catch (IOException exception) {
            ServerSelectorLogger.console("Exception thrown in UpdateMessageHandler", exception);
        }
    }
}
