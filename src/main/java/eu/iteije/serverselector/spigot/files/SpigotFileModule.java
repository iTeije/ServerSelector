package eu.iteije.serverselector.spigot.files;

import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.common.storage.StorageLocation;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;

import java.util.HashMap;

public class SpigotFileModule {

    private static HashMap<String, SpigotFile> files = new HashMap<>();

    public SpigotFileModule(ServerSelectorSpigot serverSelectorSpigot) {
        SpigotFile configFile = new SpigotFile(serverSelectorSpigot, StorageLocation.CONFIG_FILE);
        SpigotFile messagesFile = new SpigotFile(serverSelectorSpigot, StorageLocation.MESSAGE_FILE);
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
        files.put(spigotFile.getStorageLocation().getFileName(), spigotFile);
    }
}
