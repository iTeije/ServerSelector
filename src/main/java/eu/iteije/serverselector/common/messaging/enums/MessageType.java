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
        public void sendBungee(String message, String server, ServerSelectorSpigot serverSelectorSpigot, String[] playerNames) {
            if (playerNames != null) {
                for (String playerName : playerNames) {
                    serverSelectorSpigot.getCommunicationModule().sendMessage(message, "MessagePlayer", playerName);
                }
                return;
            }

            serverSelectorSpigot.getCommunicationModule().sendMessage(message, "Broadcast");
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
        public void sendBungee(String message, String server, ServerSelectorSpigot serverSelectorSpigot, String[] playerNames) {
//            serverSelectorSpigot.getCommunicationModule().sendMessage(message, "broadcast");
        }
    },

    ;

    public abstract void send(String message, Player... players);

    public abstract void sendBungee(String message, String server, ServerSelectorSpigot serverSelectorSpigot, String[] playerNames);

}
