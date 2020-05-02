package eu.iteije.serverselector.common.messaging.enums;

import org.bukkit.entity.Player;

public enum MessageType {

    SPIGOT_MESSAGE {
        @Override
        public void send(String message, Player... players) {
            for (Player player : players) {
                player.sendMessage(message);
            }
        }
    },
    SPIGOT_TITLE {
        @Override
        public void send(String message, Player... players) {
            for (Player player : players) {
                // Subtitle can't be null or it won't be send, check spigot api for more information
                player.sendTitle(message, "", 20, 60, 20);
            }
        }
    },
    BUNGEE_MESSAGE {
        @Override
        public void send(String message, Player... players) {

        }
    },
    BUNGEE_TITLE {
        @Override
        public void send(String message, Player... players) {

        }
    };

    public abstract void send(String message, Player... players);

}
