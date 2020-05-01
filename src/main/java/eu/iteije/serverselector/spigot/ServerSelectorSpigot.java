package eu.iteije.serverselector.spigot;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.platform.Platform;
import eu.iteije.serverselector.spigot.messaging.BungeeMessage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class ServerSelectorSpigot extends JavaPlugin {

    // Spigot module instances
    BungeeMessage bungeeMessage;

    // Spigot plugin instance
    @Getter private static ServerSelectorSpigot instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        // If the plugin fails to make a new instance of ServerSelector it won't work -> shut down
        try {
            new ServerSelector(Platform.SPIGOT);
        } catch (Exception exception) {
            exception.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        bungeeMessage = new BungeeMessage(this);

        bungeeMessage.sendMessage("HALLO FELLOW PEOPLE");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ServerSelector.getInstance().disable();
    }
}
