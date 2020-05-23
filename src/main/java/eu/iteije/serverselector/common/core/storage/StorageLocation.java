package eu.iteije.serverselector.common.core.storage;

import lombok.Getter;

public enum StorageLocation {

    CONFIG_FILE("config.yml"),
    MESSAGE_FILE("messages.yml"),

    MENU_FOLDER("menus"),


    ;

    @Getter private String fileName;
    StorageLocation(String fileName) {
        this.fileName = fileName;
    }

}
