package eu.iteije.serverselector.spigot.files;

import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.core.storage.StorageLocation;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;

import java.util.HashMap;

public class SpigotFileModule {

    private static HashMap<String, SpigotFile> files = new HashMap<>();
    private static HashMap<String, SpigotFolder> folders = new HashMap<>();


    public SpigotFileModule(ServerSelectorSpigot serverSelectorSpigot) {
        SpigotFile configFile = new SpigotFile(serverSelectorSpigot, StorageLocation.CONFIG_FILE);
        SpigotFile messagesFile = new SpigotFile(serverSelectorSpigot, StorageLocation.MESSAGE_FILE);

        SpigotFolder menuFolder = new SpigotFolder(serverSelectorSpigot, StorageLocation.MENU_FOLDER);
        menuFolder.load();

        // Initialize default plugin files
        new SpigotFile(serverSelectorSpigot, StorageLocation.MENU_FOLDER.getFileName() + "/Main.json");
        new SpigotFile(serverSelectorSpigot, StorageLocation.MENU_FOLDER.getFileName() + "/Examples.json");
    }

    /**
     * @param name name of the requested file (including .yml)
     * @return file (could be null)
     */
    public static SpigotFile getFileByName(String name) {
        return files.get(name);
    }

    public static SpigotFile getFile(StorageKey key) {
        return files.get(key.getStorageLocation().getFileName());
    }

    public static void saveFile(SpigotFile spigotFile) {
        files.put(spigotFile.getFileName(), spigotFile);
    }

    public static void saveFolder(SpigotFolder spigotFolder) {
        folders.put(spigotFolder.getFolderName(), spigotFolder);
    }

    public static SpigotFolder getFolderByName(String name) {
        return folders.get(name);
    }
}
