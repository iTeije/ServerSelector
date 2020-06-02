package eu.iteije.serverselector.spigot;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.platform.Platform;
import eu.iteije.serverselector.spigot.commands.SpigotCommandModule;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.menus.MenuModule;
import eu.iteije.serverselector.spigot.messaging.SpigotCommunicationModule;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import eu.iteije.serverselector.spigot.players.PlayerModule;
import eu.iteije.serverselector.spigot.selector.SelectorModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class ServerSelectorSpigot extends JavaPlugin {

    // Spigot module instances
    private SpigotCommandModule commandModule;
    private SpigotFileModule fileModule;
    private SpigotMessageModule messageModule;
    private SpigotCommunicationModule communicationModule;

    private PlayerModule playerModule;
    private MenuModule menuModule;
    private SelectorModule selectorModule;

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

        // Load modules
        this.commandModule = new SpigotCommandModule(this);
        this.fileModule = new SpigotFileModule(this);
        this.messageModule = new SpigotMessageModule();
        this.communicationModule = new SpigotCommunicationModule(this);
        this.playerModule = new PlayerModule(this);
        this.menuModule = new MenuModule(this);
        this.selectorModule = new SelectorModule(this);

        // Register online players
        this.playerModule.registerPlayers();

        // Cache menus
        this.selectorModule.cacheMenus();
    }

    @Override
    public void onDisable() {
        // Clear player cache
        playerModule.clearCache();

        // Destroy status runnables
        selectorModule.getStatusUpdater().destroyTasks();

        // Plugin shutdown logic
        ServerSelector.getInstance().disable();
    }
}
