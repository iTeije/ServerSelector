package eu.iteije.serverselector.common.core.storage;

import lombok.Getter;

public enum StorageKey {

    /**
     * COMMON
     */

    PERMISSION_ERROR("permission_error", StorageLocation.MESSAGE_FILE),

    COMMAND_NOT_FOUND("command_not_found", StorageLocation.MESSAGE_FILE),
    COMMAND_ERROR("command_error", StorageLocation.MESSAGE_FILE),
    COMMAND_PLAYER_ONLY("command_player_only", StorageLocation.MESSAGE_FILE),
    COMMAND_CONSOLE_ONLY("command_console_only", StorageLocation.MESSAGE_FILE),


    /**
     * ADMIN
     */

    // Message menu
    MESSAGE_MENU_NAME("message_menu_name", StorageLocation.MESSAGE_FILE),
    MESSAGE_MENU_TITLE("message_menu_title", StorageLocation.MESSAGE_FILE),
    MESSAGE_MENU_SUCCESS("message_menu_success", StorageLocation.MESSAGE_FILE),

    RELOAD_STARTED("reload_started", StorageLocation.MESSAGE_FILE),
    RELOAD_FINISHED_LOCAL("reload_finished_local", StorageLocation.MESSAGE_FILE),
    RELOAD_FINISHED_PLAYERS("reload_finished_players", StorageLocation.MESSAGE_FILE),
    RELOAD_FINISHED("reload_finished", StorageLocation.MESSAGE_FILE),


    /**
     * GLOBAL
     */

    HELP_COMMAND_TITLE("help_command_title", StorageLocation.MESSAGE_FILE),
    HELP_COMMAND_DEDICATED("help_command_dedicated", StorageLocation.MESSAGE_FILE),
    HELP_COMMAND_ITEM("help_command_item", StorageLocation.MESSAGE_FILE),
    HELP_COMMAND_NO_RESULTS("help_command_no_results", StorageLocation.MESSAGE_FILE),

    // Action queue messages
    ACTIONQUEUE_PROPOSE_CHAT("actionqueue_propose_chat", StorageLocation.MESSAGE_FILE),
    ACTIONQUEUE_CANCELLED("actionqueue_cancelled", StorageLocation.MESSAGE_FILE),

    // Menu messages
    MENU_OPENING("menu_opening", StorageLocation.MESSAGE_FILE),
    MENU_CLOSE("menu_close", StorageLocation.MESSAGE_FILE),
    MENU_NEXT_PAGE("menu_next_page", StorageLocation.MESSAGE_FILE),
    MENU_PREVIOUS_PAGE("menu_previous_page", StorageLocation.MESSAGE_FILE),

    // Menu action messages
    ACTION_MENU_FAILED("action_menu_failed", StorageLocation.MESSAGE_FILE),
    ACTION_SOUND_FAILED("action_sound_failed", StorageLocation.MESSAGE_FILE),
    ACTION_TELEPORT_INVALID("action_teleport_invalid", StorageLocation.MESSAGE_FILE),
    ACTION_EFFECT_FAILED("action_effect_failed", StorageLocation.MESSAGE_FILE),

    // Main menu
    STATUS_OFFLINE("status_offline", StorageLocation.MESSAGE_FILE),
    STATUS_WHITELISTED("status_whitelisted", StorageLocation.MESSAGE_FILE),
    STATUS_ONLINE("status_online", StorageLocation.MESSAGE_FILE),


    /**
     * BUNGEE
     */

    // Queue and send messages
    SEND_PROCESSING("send_processing", StorageLocation.MESSAGE_FILE),
    SEND_ALREADY_CONNECTED("send_already_connected", StorageLocation.MESSAGE_FILE),
    SEND_SERVER_NOT_FOUND("send_server_not_found", StorageLocation.MESSAGE_FILE),
    SEND_SERVER_UNAVAILABLE("send_server_unavailable", StorageLocation.MESSAGE_FILE),

    QUEUE_PROCESSING("queue_processing", StorageLocation.MESSAGE_FILE),
    QUEUE_ALREADY_QUEUED("queue_already_queued", StorageLocation.MESSAGE_FILE),
    QUEUE_NOT_QUEUED("queue_not_queued", StorageLocation.MESSAGE_FILE),
    QUEUE_LEFT("queue_left", StorageLocation.MESSAGE_FILE),


    /**
     * CONFIG ITEMS
     */

    // Selector item
    CONFIG_SELECTOR_ENABLED("selector-enabled", StorageLocation.CONFIG_FILE),
    CONFIG_SELECTOR_ITEM("selector-item", StorageLocation.CONFIG_FILE),
    CONFIG_SELECTOR_NAME("selector-name", StorageLocation.CONFIG_FILE),
    CONFIG_SELECTOR_SLOT("selector-slot", StorageLocation.CONFIG_FILE),
    CONFIG_SELECTOR_MOVE("selector-move", StorageLocation.CONFIG_FILE),
    CONFIG_SELECTOR_DROP("selector-drop", StorageLocation.CONFIG_FILE),

    // Server pinging settings
    CONFIG_UPDATE_DELAY("update-delay", StorageLocation.CONFIG_FILE),
    CONFIG_FETCH_DELAY("fetch-delay", StorageLocation.CONFIG_FILE),
    CONFIG_OFFLINE_TIME("offline-time", StorageLocation.CONFIG_FILE),
    CONFIG_QUEUE_DELAY("queue-delay", StorageLocation.CONFIG_FILE),
    CONFIG_BUNGEE_IP("bungee-ip", StorageLocation.CONFIG_FILE),


    ;

    @Getter private String path;
    @Getter private StorageLocation storageLocation;
    StorageKey(String path, StorageLocation storageLocation) {
        this.path = path;
        this.storageLocation = storageLocation;
    }

}
