package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportAction extends Action {

    private SpigotMessageModule messageModule;

    public TeleportAction(ServerSelectorSpigot instance) {
        super(ActionTag.PLAYER);
        this.messageModule = instance.getMessageModule();
    }

    @Override
    public void execute(String context, Player player) {
        String[] coordinates = context.split("!");
        if (coordinates.length >= 4) {
            try {
                Location location;
                World world = Bukkit.getWorld(coordinates[0]);
                double x = Double.parseDouble(coordinates[1]);
                double y = Double.parseDouble(coordinates[2]);
                double z = Double.parseDouble(coordinates[3]);
                if (coordinates.length == 6) {
                    float yaw = Float.parseFloat(coordinates[4]);
                    float pitch = Float.parseFloat(coordinates[5]);
                    location = new Location(world, x, y, z, yaw, pitch);
                } else {
                    location = new Location(world, x, y, z);
                }
                player.teleport(location);
            } catch (Exception exception) {
                // Send invalid coordinates message
                messageModule.sendToPlayer(StorageKey.ACTION_TELEPORT_INVALID, new Player[]{player}, MessageType.MESSAGE);
            }
        } else {
            // Send invalid coordinates message
            messageModule.sendToPlayer(StorageKey.ACTION_TELEPORT_INVALID, new Player[]{player}, MessageType.MESSAGE);
        }

        player.closeInventory();
    }
}
