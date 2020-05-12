package eu.iteije.serverselector.spigot.players;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.players.listeners.PlayerChatListener;
import eu.iteije.serverselector.spigot.players.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.UUID;

public class PlayerModule {

    private ServerSelectorSpigot instance;

    public static HashMap<UUID, ServerSelectorPlayer> players = new HashMap<>();

    public PlayerModule(ServerSelectorSpigot serverSelectorSpigot) {
        this.instance = serverSelectorSpigot;

        PluginManager pluginManager = instance.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerChatListener(instance), instance);
        pluginManager.registerEvents(new PlayerJoinListener(instance), instance);
    }

    public void registerPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            registerPlayer(player);
        }
    }

    public void registerPlayer(Player player) {
        ServerSelectorPlayer serverSelectorPlayer = new ServerSelectorPlayer(instance, player.getUniqueId());
        players.put(player.getUniqueId(), serverSelectorPlayer);
    }

    public void clearCache() {
        players.clear();
        registerPlayers();
    }

    public ServerSelectorPlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

}
