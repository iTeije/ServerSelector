package eu.iteije.serverselector.common.storage;

import lombok.Getter;

public enum StorageKey {

    PERMISSION_ERROR("permission_error", StorageLocation.MESSAGE_FILE),

    // Global command related stuff
    COMMAND_NOT_FOUND("command_not_found", StorageLocation.MESSAGE_FILE),
    COMMAND_ERROR("command_error", StorageLocation.MESSAGE_FILE),
    COMMAND_PLAYER_ONLY("command_player_only", StorageLocation.MESSAGE_FILE),
    COMMAND_CONSOLE_ONLY("command_console_only", StorageLocation.MESSAGE_FILE),

    // Help command
    HELP_COMMAND_TITLE("help_command_title", StorageLocation.MESSAGE_FILE),
    HELP_COMMAND_DEDICATED("help_command_dedicated", StorageLocation.MESSAGE_FILE),
    HELP_COMMAND_ITEM("help_command_item", StorageLocation.MESSAGE_FILE),
    HELP_COMMAND_NO_RESULTS("help_command_no_results", StorageLocation.MESSAGE_FILE),

    // Action message queue
    ACTION_PROPOSE_CHAT("action_propose_chat", StorageLocation.MESSAGE_FILE),

    // Menu messages
    MENU_OPENING("menu_opening", StorageLocation.MESSAGE_FILE),
    MENU_CLOSE("menu_close", StorageLocation.MESSAGE_FILE),
    MENU_NEXT_PAGE("menu_next_page", StorageLocation.MESSAGE_FILE),
    MENU_PREVIOUS_PAGE("menu_previous_page", StorageLocation.MESSAGE_FILE),

    // Message menu
    MESSAGE_MENU_NAME("message_menu_name", StorageLocation.MESSAGE_FILE),
    MESSAGE_MENU_TITLE("message_menu_title", StorageLocation.MESSAGE_FILE),
    MESSAGE_MENU_SUCCESS("message_menu_success", StorageLocation.MESSAGE_FILE),

    // Selector item
    CONFIG_SELECTOR("selector", StorageLocation.CONFIG_FILE),
    CONFIG_SELECTOR_ITEM("selector_item", StorageLocation.CONFIG_FILE),
    CONFIG_SELECTOR_NAME("selector_name", StorageLocation.CONFIG_FILE),
    CONFIG_SELECTOR_SLOT("selector_slot", StorageLocation.CONFIG_FILE),

    // Menu action messages
    MENU_ACTION_MENU_FAILED("menu_action_menu_failed", StorageLocation.MESSAGE_FILE),

    // Main menu
    STATUS_OFFLINE("status_offline", StorageLocation.MESSAGE_FILE),
    STATUS_WHITELISTED("status_whitelisted", StorageLocation.MESSAGE_FILE),
    STATUS_ONLINE("status_online", StorageLocation.MESSAGE_FILE),

    // Selector menus settings
    SELECTOR_UPDATE_DELAY("selector_update_delay", StorageLocation.CONFIG_FILE),

    // Queue and send messages
    SEND_PROCESSING("send_processing", StorageLocation.MESSAGE_FILE),
    SEND_ALREADY_CONNECTED("send_already_connected", StorageLocation.MESSAGE_FILE),
    SEND_SERVER_NOT_FOUND("send_server_not_found", StorageLocation.MESSAGE_FILE),
    SEND_SERVER_WHITELISTED("send_server_whitelisted", StorageLocation.MESSAGE_FILE),

    ;

    @Getter private String path;
    @Getter private StorageLocation storageLocation;
    StorageKey(String path, StorageLocation storageLocation) {
        this.path = path;
        this.storageLocation = storageLocation;
    }

}
