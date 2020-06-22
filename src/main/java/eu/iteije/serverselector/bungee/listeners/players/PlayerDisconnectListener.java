package eu.iteije.serverselector.bungee.listeners.players;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.DataInputStream;

public class PlayerDisconnectListener implements Listener {

    private ServerSelectorBungee instance;

    public PlayerDisconnectListener(ServerSelectorBungee instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        // Leave the queue (leave queue handler checks whether the player is in queue or not)
        String uuid = event.getPlayer().getUniqueId().toString();
        DataInputStream inputStream = ServerSelector.getInstance().getMessageModule().getDataInputStream(new String[]{uuid});
        instance.getCommunicationModule().getHandler("LeaveQueue").process(inputStream, null);
    }
}
