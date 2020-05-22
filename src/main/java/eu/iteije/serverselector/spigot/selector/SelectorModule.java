package eu.iteije.serverselector.spigot.selector;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.files.SpigotFolder;
import eu.iteije.serverselector.spigot.menus.MenuModule;
import eu.iteije.serverselector.common.clients.objects.ServerData;
import eu.iteije.serverselector.spigot.services.menus.Item;
import eu.iteije.serverselector.spigot.services.menus.menu.Menu;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectorModule {

    private ServerSelectorSpigot instance;
    private ActionManager actionManager;
    @Getter private MenuUpdater menuUpdater;

    public SelectorModule(ServerSelectorSpigot instance) {
        this.instance = instance;
        this.actionManager = new ActionManager(instance);

        this.menuUpdater = new MenuUpdater(instance);
        this.menuUpdater.initializeUpdateScheduler();
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
            Item item = new Item(material);
            item.setName((String) itemData.get("display_name"));

            String actionType = (String) itemData.get("action_type");
            if (!actionType.equals("NONE")) {
                if (actionType.equals("CLOSE")) {
                    item.onClick(((player, item1) -> player.closeInventory()));
                } else {
                    item.onClick(((player, item1) -> {
                        actionManager.getActionByName(actionType).execute((String) itemData.get("action"), player);
                    }));
                }
            }

            try {
                JSONArray jsonLore = (JSONArray) itemData.get("lore");
                if (jsonLore != null && jsonLore.size() != 0) {
                    List<String> lore = new ArrayList<>();
                    for (int i = 0; i < jsonLore.size(); i++) {
                        String line = (String) jsonLore.get(i);
                        if (actionType.equalsIgnoreCase("SEND") || actionType.equalsIgnoreCase("QUEUE")) {
                            String server = (String) itemData.get("action");
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
        Bukkit.broadcastMessage("SelectorModule: getting server info for server " + serverName);
        ServerData serverData = this.menuUpdater.getServerInfo(serverName);
        if (serverData == null) {
            Bukkit.broadcastMessage("SelectorModule: serverinfo is null");
        } else {
            Bukkit.broadcastMessage(serverData.currentPlayers + serverData.maxPlayers);
        }

        line = line.replace("{status}", serverData != null ? serverData.getStatus() : "OFFLINE");
        line = line.replace("{current_players}", serverData != null ? serverData.getCurrentPlayers() : "0");
        line = line.replace("{max_players}", serverData != null ? serverData.getMaxPlayers() : "0");
        line = line.replace("{queue}", "soon");

        return line;
    }

}
