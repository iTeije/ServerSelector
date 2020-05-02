package eu.iteije.serverselector.common.messaging.enums;

import lombok.Getter;

public enum ReplacementType {

    VARIABLE(Color.AQUA, Format.NONE),
    VARIABLE_ERROR(Color.DARK_RED, Format.NONE),

    COMMAND(Color.AQUA, Format.NONE),
    COMMAND_DESCRIPTION(Color.GRAY, Format.NONE),
    COMMAND_ERROR(Color.DARK_RED, Format.NONE),

    NONE(Color.NONE, Format.NONE);


    ;

    @Getter private Color color;
    @Getter private Format format;

    @Getter private final String combined;

    ReplacementType(Color color, Format format) {
        this.color = color;
        this.format = format;
        this.combined = color.getChatCode() + format.getChatCode();
    }
}
