package eu.iteije.serverselector.common.storage;

import java.util.Set;

public interface ServerSelectorFile {

    // Get data from specified storage
    String getString(StorageKey storageKey);
    Integer getInt(StorageKey storageKey);
    Boolean getBoolean(StorageKey storageKey);

    String getStringFromPath(String path);
    Integer getIntFromPath(String path);
    Boolean getBooleanFromPath(String path);

    Set<String> getStringSet(String path);

    // Set data to specified storage key
    void setString(String string, StorageKey storageKey);
    void setInt(int integer, StorageKey storageKey);

    void setStringToPath(String string, String path);
    void setIntToPath(int integer, String path);

    // Other stuff
    void reload();
    void save();
    boolean hasFile(String fileName);

}
