package eu.iteije.serverselector;

import eu.iteije.serverselector.common.platform.Platform;
import lombok.Getter;

@Getter
public final class ServerSelector {

    // Common module instances

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

    }

    public void disable() {
        // Nothing here yet
    }
}
