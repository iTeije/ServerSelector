package eu.iteije.serverselector.bungee.files;

import eu.iteije.serverselector.bungee.ServerSelectorBungee;
import eu.iteije.serverselector.common.core.storage.ServerSelectorFile;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.core.storage.StorageLocation;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class BungeeFile implements ServerSelectorFile {

    @Getter private Configuration configuration;
    @Getter private StorageLocation storageLocation;
    @Getter private String fileName;
    @Getter public File file;

    private ServerSelectorBungee serverSelectorBungee;

    public BungeeFile(ServerSelectorBungee serverSelectorBungee, StorageLocation storageLocation) {
        this(serverSelectorBungee, storageLocation.getFileName());
    }

    public BungeeFile(ServerSelectorBungee serverSelectorBungee, String fileName) {
        this.serverSelectorBungee = serverSelectorBungee;

        hasFile(fileName);
        this.configuration = getFile(fileName);

        this.fileName = fileName;

        BungeeFileModule.saveFile(this);
    }

    /**
     * @param storageKey the storage key (including the path)
     * @return requested string
     */
    @Override
    public String getString(StorageKey storageKey) {
        return this.configuration.getString(storageKey.getPath());
    }


    /**
     * @param storageKey the storage key (including the path)
     * @return requested integer
     */
    @Override
    public Integer getInt(StorageKey storageKey) {
        return this.configuration.getInt(storageKey.getPath());
    }


    /**
     * @param storageKey the storage key (including the path)
     * @return requested boolean
     */
    @Override
    public Boolean getBoolean(StorageKey storageKey) {
        return this.configuration.getBoolean(storageKey.getPath());
    }


    /**
     * @param path the storage key (including the path)
     * @return requested string
     */
    @Override
    public String getStringFromPath(String path) {
        return this.configuration.getString(path);
    }


    /**
     * @param path the storage key (including the path)
     * @return requested string
     */
    @Override
    public Integer getIntFromPath(String path) {
        return this.configuration.getInt(path);
    }


    /**
     * @param path the storage key (including the path)
     * @return requested string
     */
    @Override
    public Boolean getBooleanFromPath(String path) {
        return this.configuration.getBoolean(path);
    }


    /**
     * @param path data path
     * @return set of requested keys (could be empty, can't be null)
     */
    @Override
    public Set<String> getStringSet(String path) {
        Configuration section = configuration.getSection(path);

        if (section == null) return new HashSet<>();
        return new HashSet<>(section.getKeys());
    }


    /**
     * @param string the new value
     * @param storageKey storage key (contains path)
     */
    @Override
    public void setString(String string, StorageKey storageKey) {
        configuration.set(storageKey.getPath(), string);
    }


    /**
     * @param integer the new value
     * @param storageKey storage key (contains path)
     */
    @Override
    public void setInt(int integer, StorageKey storageKey) {
        configuration.set(storageKey.getPath(), integer);
    }


    /**
     * @param path path to write the message to
     * @param message the actual message
     */
    @Override
    public void setStringToPath(String path, String message) {
        configuration.set(path, message);
    }


    /**
     * @param integer the new value
     * @param path path
     */
    @Override
    public void setIntToPath(int integer, String path) {
        configuration.set(path, integer);
    }


    public void reload() {
        // Prevent plugin failures
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.configuration, new File(serverSelectorBungee.getDataFolder(), fileName));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public boolean hasFile(String fileName) {
        if (!serverSelectorBungee.getDataFolder().exists())
            serverSelectorBungee.getDataFolder().mkdir();

        File file = new File(serverSelectorBungee.getDataFolder(), fileName);

        if (!file.exists()) {
            try (InputStream inputStream = serverSelectorBungee.getResourceAsStream(fileName)) {
                Files.copy(inputStream, file.toPath());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return true;
    }

    public Configuration getFile(String fileName) {
        Configuration configuration = null;
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(serverSelectorBungee.getDataFolder(), fileName));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return configuration;
    }

}
