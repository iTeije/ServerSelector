package eu.iteije.serverselector.bungee;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.platform.Platform;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class ServerSelectorBungee extends Plugin implements Listener {

    // Bungee module instances

    // Bungee plugin instance
    @Getter private static ServerSelectorBungee instance;

    @Override
    public void onEnable() {
        instance = this;

        // If the plugin fails to make a new instance of ServerSelector it won't work -> shut down
        try {
            new ServerSelector(Platform.BUNGEE);
        } catch (Exception exception) {
            exception.printStackTrace();
            this.onDisable();
            return;
        }

    }

    @Override
    public void onDisable() {
        ServerSelector.getInstance().disable();
    }
}
