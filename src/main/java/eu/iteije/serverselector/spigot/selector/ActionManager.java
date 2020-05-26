package eu.iteije.serverselector.spigot.selector;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.selector.actions.*;
import eu.iteije.serverselector.spigot.selector.actions.objects.Action;

import java.util.HashMap;

public class ActionManager {

    public HashMap<String, Action> actions = new HashMap<>();

    public ActionManager(ServerSelectorSpigot instance) {
        registerAction("MENU", new MenuAction(instance));
        registerAction("QUEUE", new QueueAction(instance));
        registerAction("SEND", new SendAction(instance));
        registerAction("CLOSE", new CloseAction());
        registerAction("MESSAGE", new MessageAction(instance));
        registerAction("CONSOLE_COMMAND", new ConsoleCommandAction(instance));
        registerAction("PLAYER_COMMAND", new PlayerCommandAction());
        registerAction("SOUND", new PlaySoundAction(instance));
    }

    public void registerAction(String name, Action action) {
        actions.put(name, action);
    }

    public Action getActionByName(String name) {
        return actions.get(name);
    }

}
