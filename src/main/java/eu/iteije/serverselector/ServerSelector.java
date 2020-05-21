package eu.iteije.serverselector;

import eu.iteije.serverselector.common.commands.CommandModule;
import eu.iteije.serverselector.common.messaging.MessageModule;
import eu.iteije.serverselector.common.platform.Platform;
import lombok.Getter;

@Getter
public final class ServerSelector {

    // Common module instances
    private CommandModule commandModule;
    private MessageModule messageModule;

    // Common plugin instance
    @Getter private static ServerSelector instance;

    // Current platform
    @Getter private final Platform platform;

    public ServerSelector(Platform platform) {
        instance = this;

        this.platform = platform;

        if (platform == Platform.SPIGOT) {

        }

        // Define modules
        this.commandModule = new CommandModule();
        this.messageModule = new MessageModule();

    }

    public void disable() {
        // Nothing here yet
    }
}
