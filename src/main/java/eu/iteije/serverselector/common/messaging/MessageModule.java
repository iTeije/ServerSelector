package eu.iteije.serverselector.common.messaging;

import eu.iteije.serverselector.common.messaging.objects.Replacement;
import eu.iteije.serverselector.common.storage.StorageKey;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import org.bukkit.ChatColor;

public class MessageModule {

    public MessageModule() {

    }

    public String convert(StorageKey storageKey,  Replacement... replacements) {
        String message = SpigotFileModule.getFile(storageKey).getString(storageKey);

        // Convert all replacements
        for (Replacement replacement : replacements) {
            message = message.replace(replacement.getKey(), replacement.getReplacementType().getCombined() + replacement.getReplacement());
        }

        // Convert color codes
        message = convertColorCodes(message);

        return message;
    }

    public String convert(String message,  Replacement... replacements) {
        // Convert all replacements
        for (Replacement replacement : replacements) {
            message = message.replace(replacement.getKey(), replacement.getReplacementType().getCombined() + replacement.getReplacement());
        }

        // Convert color codes
        message = convertColorCodes(message);

        return message;
    }

    private String convertColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
