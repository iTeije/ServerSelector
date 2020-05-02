package eu.iteije.serverselector.common.messaging.enums;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import org.bukkit.entity.Player;

public enum MessageType {

    MESSAGE {
        @Override
        public void send(String message, Player... players) {
            for (Player player : players) {
                player.sendMessage(message);
            }
        }

        @Override
        public void sendBungee(String message, String server, ServerSelectorSpigot serverSelectorSpigot, Player... players) {
            for (Player player : players) {
                serverSelectorSpigot.getCommunicationModule().sendMessage(message, player);
            }

        }
    },
    TITLE {
        @Override
        public void send(String message, Player... players) {
            for (Player player : players) {
                // Subtitle can't be null or it won't be send, check spigot api for more information
                player.sendTitle(message, "", 20, 60, 20);
            }
        }

        @Override
        public void sendBungee(String message, String server, ServerSelectorSpigot serverSelectorSpigot, Player... players) {

        }
    },

    ;

    public abstract void send(String message, Player... players);
    public abstract void sendBungee(String message, String server, ServerSelectorSpigot serverSelectorSpigot, Player... players);

}
