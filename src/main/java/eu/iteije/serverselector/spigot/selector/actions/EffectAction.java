package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.atomic.AtomicBoolean;

public class EffectAction extends Action {

    private SpigotMessageModule messageModule;

    public EffectAction(ServerSelectorSpigot instance) {
        super(ActionTag.PLAYER);
        this.messageModule = instance.getMessageModule();
    }

    @Override
    public void execute(String context, Player player) {
        String[] settings = context.split("!");
        try {
            if (settings.length >= 1) {
                settings[0] = settings[0].toUpperCase();

                PotionEffectType effectType = PotionEffectType.getByName(settings[0]);
                if (effectType == null) throw new Exception("PotionEffectType " + settings[0] + " does not exist");

                AtomicBoolean has = new AtomicBoolean(false);

                player.getActivePotionEffects().forEach((potionEffect -> {
                    if (potionEffect.getType().equals(effectType)) {
                        has.set(true);
                    }
                }));

                if (has.get()) {
                    player.removePotionEffect(effectType);
                } else {
                    int amplifier = settings[1] != null ? Integer.parseInt(settings[1]) : 1;
                    int duration = settings[2] != null ? Integer.parseInt(settings[2]) : 300;

                    player.addPotionEffect(effectType.createEffect(duration * 20, amplifier - 1));
                }
            } else throw new Exception("No effect given");
        } catch (Exception exception) {
            // Send effect failed
            messageModule.sendToPlayer(StorageKey.ACTION_EFFECT_FAILED, new Player[]{player}, MessageType.MESSAGE,
                    new Replacement("{effect}", settings[0]));
            ServerSelectorLogger.console("Error occurred while adding effect. ", exception);
        }

        player.closeInventory();
    }
}
