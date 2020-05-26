package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PlaySoundAction extends Action {

    private ServerSelectorSpigot instance;
    private List<Sound> sounds;

    public PlaySoundAction(ServerSelectorSpigot instance) {
        super(ActionTag.PLAYER);
        this.instance = instance;

        this.sounds = Arrays.asList(Sound.values());
    }

    @Override
    public void execute(String context, Player player) {
        try {
            if (sounds.contains(Sound.valueOf(context))) {
                player.playSound(player.getLocation(), Sound.valueOf(context), 10, 1);
            }
        } catch (IllegalArgumentException exception) {
            // Send sound not found message
            SpigotMessageModule messageModule = instance.getMessageModule();
            messageModule.sendToPlayer(StorageKey.ACTION_SOUND_FAILED, new Player[]{player}, MessageType.MESSAGE,
                    new Replacement("{sound}", context));
        }

        player.closeInventory();
    }
}
