package eu.iteije.serverselector.common.messaging.enums;

import lombok.Getter;

public enum MessageChannel {

    BUNGEE_GLOBAL("bungee:global"),

    ;

    @Getter private String channel;

    MessageChannel(String channel) {
        this.channel = channel;
    }
}
