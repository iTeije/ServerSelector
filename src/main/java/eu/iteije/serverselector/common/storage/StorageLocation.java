package eu.iteije.serverselector.common.storage;

import lombok.Getter;

public enum StorageLocation {

    CONFIG_FILE("config.yml"),
    MESSAGE_FILE("messages.yml"),


    ;

    @Getter private String fileName;
    StorageLocation(String fileName) {
        this.fileName = fileName;
    }

}
