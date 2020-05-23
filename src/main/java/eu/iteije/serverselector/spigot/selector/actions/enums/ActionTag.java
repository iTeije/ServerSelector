package eu.iteije.serverselector.spigot.selector.actions.enums;

public enum ActionTag {

    // Bungee tag: send, queue (where the player eventually switches server)
    BUNGEE,
    // Player tag: send message, toggle effect, toggle players, command
    PLAYER,
    // Others tag: console command, bungee command
    OTHERS

    ;

    ActionTag() {

    }

}
