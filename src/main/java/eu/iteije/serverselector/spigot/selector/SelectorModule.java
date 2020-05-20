package eu.iteije.serverselector.spigot.selector;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.files.SpigotFolder;
import eu.iteije.serverselector.spigot.menus.MenuModule;
import eu.iteije.serverselector.spigot.services.menus.menu.Menu;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.FileReader;

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
            try (FileReader reader = new FileReader("menus/" + menuFile.getName())) {
                // Read JSON file
                JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

                menuModule.cacheMenu(parseMenuObject(jsonObject), FilenameUtils.removeExtension(menuFile.getName()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public Menu parseMenuObject(JSONObject menu) {
        
    }


}
