package eu.iteije.serverselector.spigot.selector;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.files.SpigotFolder;
import eu.iteije.serverselector.spigot.menus.MenuModule;
import eu.iteije.serverselector.spigot.services.menus.Item;
import eu.iteije.serverselector.spigot.services.menus.menu.Menu;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

public class SelectorModule {

    private ServerSelectorSpigot instance;

    public SelectorModule(ServerSelectorSpigot instance) {
        this.instance = instance;
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

        Bukkit.broadcastMessage(items.toJSONString());

        Iterator iterator = items.iterator();

        // Loop through all JSON item elements
        int index = 0;
        while (iterator.hasNext()) {
            JSONObject itemData = (JSONObject) items.get(index);

            Material material = Material.getMaterial((String) itemData.get("material"));
            Item item = new Item(material);
            item.setName((String) itemData.get("display_name"));

            menu.setItem(Integer.parseInt(String.valueOf(itemData.get("slot"))), item);
            iterator.next();

            index++;
        }

        return menu;
    }

}
