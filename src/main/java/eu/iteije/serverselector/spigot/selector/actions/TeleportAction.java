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
        String[] arguments = context.split("\\|");
        if (arguments.length >= 4) {
            try {
                Location location;
                World world = Bukkit.getWorld(arguments[0]);
                double x = Double.parseDouble(arguments[1]);
                double y = Double.parseDouble(arguments[2]);
                double z = Double.parseDouble(arguments[3]);
                if (arguments.length == 6) {
                    float yaw = Float.parseFloat(arguments[4]);
                    float pitch = Float.parseFloat(arguments[5]);
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
    }
}
