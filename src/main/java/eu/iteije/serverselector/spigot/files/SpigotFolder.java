package eu.iteije.serverselector.spigot.files;

import eu.iteije.serverselector.common.storage.StorageLocation;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import lombok.Getter;

import java.io.File;

public class SpigotFolder {

    private ServerSelectorSpigot instance;
    private String folderName;
    @Getter private StorageLocation storageLocation;

    @Getter public File folder;

    public SpigotFolder(ServerSelectorSpigot instance, StorageLocation storageLocation) {
        this.instance = instance;
        this.folderName = storageLocation.getFileName();
        this.storageLocation = storageLocation;

        SpigotFileModule.saveFolder(this);
    }

    public synchronized void load() {
        this.folder = new File(instance.getDataFolder() + File.separator + folderName);

        if (!folder.exists()) {
            folder.mkdir();
        }
    }

}
