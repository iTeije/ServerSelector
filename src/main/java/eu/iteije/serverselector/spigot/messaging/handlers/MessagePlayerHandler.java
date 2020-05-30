package eu.iteije.serverselector.spigot.messaging.handlers;

import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.spigot.messaging.interfaces.SpigotCommunicationImplementation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.DataInputStream;
import java.io.IOException;

public class MessagePlayerHandler implements SpigotCommunicationImplementation {

    public MessagePlayerHandler() {

    }

    @Override
    public void process(DataInputStream input) {
        try {
            // Read message
            String message = input.readUTF();

            // Cancel message whenever the length is 0
            if (message.length() == 0) return;

            boolean command = message.charAt(0) == '/';
            if (command) message = message.substring(1);

            // Read player name
            if (input.available() >= 1) {
                String playerName = input.readUTF();
                Player player = Bukkit.getPlayer(playerName);


                // check if player is not null (dont wanna have null pointers)
                if (player != null) {
                    // If it is an command, the server will execute the command on the players behalf
                    if (command) {
                        player.performCommand(message);
                        return;
                    }
                    player.sendMessage(message);
                }
                return;
            }

            if (command) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), message);
            }
        } catch (IOException exception) {
            ServerSelectorLogger.console("IOException thrown in MessagePlayerHandler.", exception);
            exception.printStackTrace();
        }
    }
}
