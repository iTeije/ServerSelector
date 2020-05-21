package eu.iteije.serverselector.common.messaging.enums;

import lombok.Getter;

public enum Color {

    // Message colors

    BLACK("§0"),
    DARK_BLUE("§1"),
    DARK_GREEN("§2"),
    DARK_AQUA("§3"),
    DARK_RED("§4"),
    DARK_PURPLE("§5"),
    GOLD("§6"),
    GRAY("§7"),
    DARK_GRAY("§8"),
    BLUE("§9"),
    YELLOW("§e"),
    GREEN("§a"),
    AQUA("§b"),
    RED("§c"),
    LIGHT_PURPLE("§d"),
    WHITE("§f"),

    NONE(""),


    ;


    @Getter private String chatCode;

    Color(String chatCode) {
        this.chatCode = chatCode;
    }

}
