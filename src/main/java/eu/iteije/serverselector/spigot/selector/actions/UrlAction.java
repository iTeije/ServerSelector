package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.messaging.MessageModule;
import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class UrlAction extends Action {

    MessageModule messageModule;

    public UrlAction() {
        super(ActionTag.PLAYER);
        this.messageModule = ServerSelector.getInstance().getMessageModule();
    }

    @Override
    public void execute(String context, Player player) {
        String[] settings = context.split("!");

        if (settings.length >= 2) {
            settings[1] = messageModule.convertColorCodes(settings[1]);

            TextComponent textComponent = new TextComponent(settings[1]);
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, settings[0]));
            if (settings.length == 3) {
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messageModule.convertColorCodes(settings[2])).create()));
            }

            player.spigot().sendMessage(textComponent);
        }

        player.closeInventory();
    }
}
