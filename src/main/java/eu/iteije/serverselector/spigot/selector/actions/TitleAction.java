package eu.iteije.serverselector.spigot.selector.actions;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.common.messaging.MessageModule;
import eu.iteije.serverselector.spigot.selector.actions.enums.ActionTag;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.entity.Player;

public class TitleAction extends Action {

    MessageModule messageModule;

    public TitleAction() {
        super(ActionTag.PLAYER);
        this.messageModule = ServerSelector.getInstance().getMessageModule();
    }

    @Override
    public void execute(String context, Player player) {
        String[] arguments = context.split("\\|");

        if (arguments.length >= 2) {
            String title = messageModule.convertColorCodes(arguments[0]);
            String subTitle = messageModule.convertColorCodes(arguments[1]);
            int fadeIn = 20;
            int stay = 40;
            int fadeOut = 20;
            if (arguments.length == 5) {
                fadeIn = Integer.parseInt(arguments[2]);
                stay = Integer.parseInt(arguments[3]);
                fadeOut = Integer.parseInt(arguments[4]);
            }
            player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
        }
    }
}
