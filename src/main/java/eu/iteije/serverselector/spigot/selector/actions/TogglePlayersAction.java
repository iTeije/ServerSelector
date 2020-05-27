package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.players.ServerSelectorPlayer;
import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.entity.Player;

public class TogglePlayersAction extends Action {

    private ServerSelectorSpigot instance;

    public TogglePlayersAction(ServerSelectorSpigot instance) {
        super(ActionTag.PLAYER);
        this.instance = instance;
    }

    @Override
    public void execute(String context, Player player) {
        ServerSelectorPlayer serverSelectorPlayer = instance.getPlayerModule().getPlayer(player.getUniqueId());
        boolean current = serverSelectorPlayer.isToggledPlayers();
        if (!current) {
            instance.getServer().getOnlinePlayers().forEach(onlinePlayer -> {
                serverSelectorPlayer.setToggledPlayers(true);
                if (!onlinePlayer.hasPermission("serverselector.actions.toggleplayers.bypass")) {
                    player.hidePlayer(instance, onlinePlayer);
                }
            });
        } else {
            instance.getServer().getOnlinePlayers().forEach(onlinePlayer -> {
                serverSelectorPlayer.setToggledPlayers(false);
                if (!onlinePlayer.hasPermission("serverselector.actions.toggleplayers.bypass")) {
                    player.showPlayer(instance, onlinePlayer);
                }
            });
        }

        player.closeInventory();
    }
}
