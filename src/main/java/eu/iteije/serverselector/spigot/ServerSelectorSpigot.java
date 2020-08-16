package eu.iteije.serverselector.spigot;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.platform.Platform;
import eu.iteije.serverselector.spigot.commands.SpigotCommandModule;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.menus.MenuModule;
import eu.iteije.serverselector.spigot.messaging.SpigotCommunicationModule;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import eu.iteije.serverselector.spigot.players.PlayerModule;
import eu.iteije.serverselector.spigot.runnables.SpigotRunnableManager;
import eu.iteije.serverselector.spigot.selector.SelectorModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

@Getter
public final class ServerSelectorSpigot extends JavaPlugin {

    private long start;

    // Spigot module instances
    private SpigotCommandModule commandModule;
    private SpigotFileModule fileModule;
    private SpigotMessageModule messageModule;
    private SpigotCommunicationModule communicationModule;

    private PlayerModule playerModule;
    private MenuModule menuModule;
    private SelectorModule selectorModule;

    private SpigotRunnableManager runnableManager;

    // Spigot plugin instance
    @Getter private static ServerSelectorSpigot instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        start = System.currentTimeMillis() / 1000L;

        // If the plugin fails to make a new instance of ServerSelector it won't work -> shut down
        try {
            new ServerSelector(Platform.SPIGOT);
        } catch (Exception exception) {
            exception.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load modules
        this.fileModule = new SpigotFileModule(this);
        this.runnableManager = new SpigotRunnableManager(this);
        this.messageModule = new SpigotMessageModule();
        this.communicationModule = new SpigotCommunicationModule(this);
        this.menuModule = new MenuModule(this);
        this.playerModule = new PlayerModule(this);
        this.selectorModule = new SelectorModule(this);
        this.commandModule = new SpigotCommandModule(this);

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

        Jedis jedis = selectorModule.getStatusUpdater().getJedis();
        if (jedis != null) jedis.close();

        // Plugin shutdown logic
        ServerSelector.getInstance().disable();
    }
}
