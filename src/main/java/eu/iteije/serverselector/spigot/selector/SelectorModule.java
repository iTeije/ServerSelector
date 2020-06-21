package eu.iteije.serverselector.spigot.selector;

import eu.iteije.serverselector.common.core.logging.ServerSelectorLogger;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.networking.objects.ServerData;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.files.SpigotFolder;
import eu.iteije.serverselector.spigot.menus.MenuModule;
import eu.iteije.serverselector.spigot.services.menus.Item;
import eu.iteije.serverselector.spigot.services.menus.menu.Menu;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SelectorModule {

    private ServerSelectorSpigot instance;
    private ActionManager actionManager;
    @Getter private StatusUpdater statusUpdater;

    public SelectorModule(ServerSelectorSpigot instance) {
        this.instance = instance;
        this.actionManager = new ActionManager(instance);

        this.statusUpdater = new StatusUpdater(instance);
        this.statusUpdater.initializeUpdateScheduler();
        this.statusUpdater.initializeFetchScheduler();

        this.statusUpdater.updateServerInfo(new HashMap<>());
    }

    public void cacheMenus() {
        SpigotFolder folder = SpigotFileModule.getFolderByName("menus");
        File[] files = folder.getFolder().listFiles((dir, name) -> name.matches(".*\\.json"));

        MenuModule menuModule = instance.getMenuModule();

        JSONParser jsonParser = new JSONParser();

        // This will probably be the longest and most ugly code I've ever wrote
        for (File menuFile : files) {
            try (FileReader reader = new FileReader(menuFile)) {
                // Read JSON file
                JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

                Menu menu = new Menu(instance, (String) jsonObject.get("title"),
                        Integer.parseInt(String.valueOf(jsonObject.get("size"))),
                        (String) jsonObject.get("chat_name"));
                menuModule.cacheMenu(parseMenuObject(jsonObject, menu), FilenameUtils.removeExtension(menuFile.getName()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public Menu parseMenuObject(JSONObject object, Menu menu) {
        // Defining a bunch of things

        // Parse all items and create a new menu object including these items
        JSONArray items = (JSONArray) object.get("items");

        Iterator iterator = items.iterator();

        // Loop through all JSON item elements
        int index = 0;
        while (iterator.hasNext()) {
            JSONObject itemData = (JSONObject) items.get(index);

            Material material = Material.getMaterial((String) itemData.get("material"));
            if (material == null) {
                ServerSelectorLogger.console("Material " + itemData.get("material") + " could not be found (Menu: " + menu.getChatName() + ")");
                break;
            }
            Item item = new Item(material);

            String displayName = (String) itemData.get("display_name");
            if (displayName != null) {
                item.setName((String) itemData.get("display_name"));
            }

            JSONArray actions = (JSONArray) itemData.get("actions");
            if (actions != null) {
                item.onClick(((player, item1) -> {
                    actionManager.processActions((JSONArray) itemData.get("actions"), player);
                }));
            }

            try {
                JSONArray jsonLore = (JSONArray) itemData.get("lore");
                if (jsonLore != null && jsonLore.size() != 0) {
                    List<String> lore = new ArrayList<>();

                    String server = (String) itemData.get("server");

                    for (int i = 0; i < jsonLore.size(); i++) {
                        String line = (String) jsonLore.get(i);

                        if (server != null) {
                            instance.getCommunicationModule().requestServerInfo(server);
                            line = convertLore(line, server);
                        }
                        lore.add(line);
                    }

                    item.setLore(lore.toArray(new String[lore.size()]));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }


            menu.setItem(Integer.parseInt(String.valueOf(itemData.get("slot"))), item);
            iterator.next();

            index++;
        }

        return menu;
    }

    public String convertLore(String line, String serverName) {
        ServerData serverData = this.statusUpdater.getServerInfo(serverName);

        boolean online;
        if (serverData != null) {
            Long allowedOfflineTime = Long.parseLong(String.valueOf(SpigotFileModule.getFile(StorageKey.CONFIG_OFFLINE_TIME).getInt(StorageKey.CONFIG_OFFLINE_TIME)));
            Long currentOfflineTime = serverData.getLastUpdate();
            long currentTime = System.currentTimeMillis() / 1000L;

            online = currentTime <= (currentOfflineTime + allowedOfflineTime);

            if (!online) statusUpdater.removeServerInfo(serverData.serverName);
        } else {
            online = false;
        }

        line = line.replace("{status}", online ? serverData.getStatus() : "OFFLINE");
        line = line.replace("{current_players}", online ? serverData.getCurrentPlayers() : "0");
        line = line.replace("{max_players}", online ? serverData.getMaxPlayers() : "0");
        line = line.replace("{queue}", serverData != null ? String.valueOf(serverData.getQueue()) : "0");

        return line;
    }

}
