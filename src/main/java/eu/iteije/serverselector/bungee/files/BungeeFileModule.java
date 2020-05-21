package eu.iteije.serverselector.bungee.files;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.common.storage.StorageLocation;

import java.util.HashMap;

public class BungeeFileModule {

    private static HashMap<String, BungeeFile> files = new HashMap<>();


    public BungeeFileModule(ServerSelectorBungee serverSelectorBungee) {
        BungeeFile messagesFile = new BungeeFile(serverSelectorBungee, StorageLocation.MESSAGE_FILE);
    }

    /**
     * @param name name of the requested file (including .yml)
     * @return file (could be null)
     */
    public static BungeeFile getFileByName(String name) {
        return files.get(name);
    }

    public static BungeeFile getFile(StorageKey key) {
        return files.get(key.getStorageLocation().getFileName());
    }

    public static void saveFile(BungeeFile bungeeFile) {
        files.put(bungeeFile.getFileName(), bungeeFile);
    }
}
