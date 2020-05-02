package eu.iteije.serverselector.bungee;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.bungee.messaging.BungeeCommunicationModule;
import eu.iteije.serverselector.common.platform.Platform;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class ServerSelectorBungee extends Plugin {

    // Bungee module instances
    private BungeeCommunicationModule communicationModule;

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

        this.communicationModule = new BungeeCommunicationModule(this);

        this.getProxy().getPluginManager().registerListener(this, communicationModule);

    }

    @Override
    public void onDisable() {
        ServerSelector.getInstance().disable();
    }
}
