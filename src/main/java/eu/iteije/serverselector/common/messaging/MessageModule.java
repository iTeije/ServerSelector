package eu.iteije.serverselector.common.messaging;

import eu.iteije.serverselector.ServerSelector;
import eu.iteije.serverselector.bungee.files.BungeeFileModule;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.platform.Platform;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import org.bukkit.ChatColor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MessageModule {

    public MessageModule() {

    }

    public String convert(StorageKey storageKey, boolean replaceColorCodes, Replacement... replacements) {
        String message;
        try {
            message = SpigotFileModule.getFile(storageKey).getString(storageKey);
        } catch (Exception exception) {
            message = BungeeFileModule.getFile(storageKey).getString(storageKey);
        }


        // Convert all replacements
        for (Replacement replacement : replacements) {
            message = message.replace(replacement.getKey(), replacement.getReplacement());
        }

        // Convert color codes
        if (replaceColorCodes) message = convertColorCodes(message);

        return message;
    }

    public String convert(String message, boolean replaceColorCodes, Replacement... replacements) {
        // Convert all replacements
        for (Replacement replacement : replacements) {
            message = message.replace(replacement.getKey(), replacement.getReplacement());
        }

        // Convert color codes
        if (replaceColorCodes) message = convertColorCodes(message);

        return message;
    }

    public String convertColorCodes(String message) {
        if (ServerSelector.getInstance().getPlatform() == Platform.BUNGEE) return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // It's a hell of a pain to look at this code, but otherwise it's complaining
    public DataInputStream getDataInputStream(String[] bytes) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            for (String byteComponent : bytes) {
                dataOutputStream.writeUTF(byteComponent);
            }

            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            return new DataInputStream(inputStream);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
