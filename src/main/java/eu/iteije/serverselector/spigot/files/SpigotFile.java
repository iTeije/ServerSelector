package eu.iteije.serverselector.spigot.files;

import eu.iteije.serverselector.common.storage.ServerSelectorFile;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.common.storage.StorageLocation;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SpigotFile implements ServerSelectorFile {

    private FileConfiguration fileConfiguration;
    @Getter private StorageLocation storageLocation;

    private ServerSelectorSpigot serverSelectorSpigot;

    public SpigotFile(ServerSelectorSpigot serverSelectorSpigot, StorageLocation storageLocation) {
        this.serverSelectorSpigot = serverSelectorSpigot;
        this.storageLocation = storageLocation;

        String fileName = storageLocation.getFileName();
        boolean hasFile = hasFile(fileName);

        // Save default
        if (!hasFile) {
            serverSelectorSpigot.saveResource(fileName, false);
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(new File(serverSelectorSpigot.getDataFolder(), fileName));

        SpigotFileModule.saveFile(this);
    }

    /**
     * @param storageKey the storage key (including the path)
     * @return requested string
     */
    @Override
    public String getString(StorageKey storageKey) {
        return this.fileConfiguration.getString(storageKey.getPath());
    }


    /**
     * @param storageKey the storage key (including the path)
     * @return requested integer
     */
    @Override
    public Integer getInt(StorageKey storageKey) {
        return this.fileConfiguration.getInt(storageKey.getPath());
    }


    /**
     * @param storageKey the storage key (including the path)
     * @return requested boolean
     */
    @Override
    public Boolean getBoolean(StorageKey storageKey) {
        return this.fileConfiguration.getBoolean(storageKey.getPath());
    }


    /**
     * @param path the storage key (including the path)
     * @return requested string
     */
    @Override
    public String getStringFromPath(String path) {
        return this.fileConfiguration.getString(path);
    }


    /**
     * @param path the storage key (including the path)
     * @return requested string
     */
    @Override
    public Integer getIntFromPath(String path) {
        return this.fileConfiguration.getInt(path);
    }


    /**
     * @param path the storage key (including the path)
     * @return requested string
     */
    @Override
    public Boolean getBooleanFromPath(String path) {
        return this.fileConfiguration.getBoolean(path);
    }


    /**
     * @param path data path
     * @return set of requested keys (could be empty, can't be null)
     */
    @Override
    public Set<String> getStringSet(String path) {
        ConfigurationSection section = fileConfiguration.getConfigurationSection(path);

        if (section == null) return new HashSet<>();
        return section.getKeys(false);
    }


    /**
     * @param string the new value
     * @param storageKey storage key (contains path)
     */
    @Override
    public void setString(String string, StorageKey storageKey) {
        fileConfiguration.set(storageKey.getPath(), string);
    }


    /**
     * @param integer the new value
     * @param storageKey storage key (contains path)
     */
    @Override
    public void setInt(int integer, StorageKey storageKey) {
        fileConfiguration.set(storageKey.getPath(), integer);
    }


    /**
     * @param string the new value
     * @param path path
     */
    @Override
    public void setStringToPath(String string, String path) {
        fileConfiguration.set(path, string);
    }


    /**
     * @param integer the new value
     * @param path path
     */
    @Override
    public void setIntToPath(int integer, String path) {
        fileConfiguration.set(path, integer);
    }


    public void reload() {
        // Prevent plugin failures
        try {
            fileConfiguration = YamlConfiguration.loadConfiguration(new File(serverSelectorSpigot.getDataFolder(), storageLocation.getFileName()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        try {
            fileConfiguration.save(new File(serverSelectorSpigot.getDataFolder(), storageLocation.getFileName()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public boolean hasFile(String fileName) {
        File file = new File(serverSelectorSpigot.getDataFolder(), fileName);
        return file.exists();
    }

}