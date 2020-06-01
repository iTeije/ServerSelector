package eu.iteije.serverselector.spigot.selector;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.selector.actions.*;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;

import java.util.HashMap;

public class ActionManager {

    public HashMap<String, Action> actions = new HashMap<>();

    private ServerSelectorSpigot instance;

    public ActionManager(ServerSelectorSpigot instance) {
        this.instance = instance;

        registerAction("MENU", new MenuAction(instance));
        registerAction("QUEUE", new QueueAction(instance));
        registerAction("SEND", new SendAction(instance));
        registerAction("CLOSE", new CloseAction());
        registerAction("MESSAGE", new MessageAction(instance));
        registerAction("CONSOLE_COMMAND", new ConsoleCommandAction(instance));
        registerAction("PLAYER_COMMAND", new PlayerCommandAction());
        registerAction("SOUND", new PlaySoundAction(instance));
        registerAction("TELEPORT", new TeleportAction(instance));
        registerAction("EFFECT", new EffectAction(instance));
        registerAction("TOGGLE_PLAYERS", new TogglePlayersAction(instance));
        registerAction("URL", new UrlAction());
        registerAction("TITLE", new TitleAction());
    }

    public void registerAction(String name, Action action) {
        actions.put(name, action);
    }

    public Action getActionByName(String name) {
        return actions.get(name);
    }

    public void processActions(JSONArray array, Player player) {
        String[] actions = new String[array.size()];

        for (int i = 0; i < array.size(); i++) {
            actions[i] = (String) array.get(i);
        }

        for (String actionElement : actions) {
            String[] arguments = actionElement.split("!");
            // Check whether the amount of arguments is equal to a direct action or a delayed action
            player.closeInventory();
            if (arguments.length == 2) {
                // No delay, find it and execute it
                getActionByName(arguments[0].toUpperCase()).execute(arguments[1], player);
            } else if (arguments.length == 4) {
                // The second given parameter is the amount of delay in ticks
                // The first one is practically ignored and just being used to show the operator its delayed
                int delay = Integer.parseInt(arguments[1]);
                Action delayedAction = getActionByName(arguments[2].toUpperCase());

                delayAction(delayedAction, arguments[3], delay, player);
            }
        }
    }

    public void delayAction(Action action, String context, int delay, Player player) {
        instance.getServer().getScheduler().runTaskLaterAsynchronously(instance, () -> {
            action.execute(context, player);
        }, delay);
    }

}
