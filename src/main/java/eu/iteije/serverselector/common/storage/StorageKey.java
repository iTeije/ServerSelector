package eu.iteije.serverselector.common.storage;

import lombok.Getter;

public enum StorageKey {

    PERMISSION_ERROR("permission_error", StorageLocation.MESSAGE_FILE),

    // Global command related stuff
    COMMAND_NOT_FOUND("command_not_found", StorageLocation.MESSAGE_FILE),
    COMMAND_ERROR("command_error", StorageLocation.MESSAGE_FILE),

    // Help command
    HELP_COMMAND_TITLE("help_command_title", StorageLocation.MESSAGE_FILE),
    HELP_COMMAND_DEDICATED("help_command_dedicated", StorageLocation.MESSAGE_FILE),
    HELP_COMMAND_ITEM("help_command_item", StorageLocation.MESSAGE_FILE),
    HELP_COMMAND_NO_RESULTS("help_command_no_results", StorageLocation.MESSAGE_FILE),

    // Menu messages
    MENU_OPENING("menu_opening", StorageLocation.MESSAGE_FILE),

    // Message menu
    MESSAGE_MENU_NAME("message_menu_name", StorageLocation.MESSAGE_FILE),
    MESSAGE_MENU_TITLE("message_menu_title", StorageLocation.MESSAGE_FILE),

    ;

    @Getter private String path;
    @Getter private StorageLocation storageLocation;
    StorageKey(String path, StorageLocation storageLocation) {
        this.path = path;
        this.storageLocation = storageLocation;
    }

}
