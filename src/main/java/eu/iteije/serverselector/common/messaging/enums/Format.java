package eu.iteije.serverselector.common.messaging.enums;

import lombok.Getter;

public enum Format {

    OBFUSCATED("§k"),
    BOLD("§l"),
    STRIKETHROUGH("§m"),
    UNDERLINE("§n"),
    ITALIC("§o"),

    NONE(""),

    ;

    @Getter private String chatCode;

    Format(String chatCode) {
        this.chatCode = chatCode;
    }

}
