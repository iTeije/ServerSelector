package eu.iteije.serverselector.bungee.files;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.core.storage.StorageLocation;

import java.util.HashMap;

public class BungeeFileModule {

    // Saved files
    private static HashMap<String, BungeeFile> files = new HashMap<>();

    public BungeeFileModule(ServerSelectorBungee serverSelectorBungee) {
        new BungeeFile(serverSelectorBungee, StorageLocation.MESSAGE_FILE);
        new BungeeFile(serverSelectorBungee, StorageLocation.CONFIG_FILE);
    }

    /**
     * @param name name of the requested file (including .yml)
     * @return saved file
     */
    public static BungeeFile getFileByName(String name) {
        return files.getOrDefault(name, null);
    }

    /**
     * @param key entry key
     * @return the BungeeFile the key is in
     */
    public static BungeeFile getFile(StorageKey key) {
        return files.get(key.getStorageLocation().getFileName());
    }

    public static void saveFile(BungeeFile bungeeFile) {
        files.put(bungeeFile.getFileName(), bungeeFile);
    }
}
