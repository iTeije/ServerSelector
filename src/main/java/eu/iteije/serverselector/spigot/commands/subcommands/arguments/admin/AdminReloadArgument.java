package eu.iteije.serverselector.spigot.commands.subcommands.arguments.admin;

import eu.iteije.serverselector.common.commands.interfaces.CommonExecutor;
import eu.iteije.serverselector.common.commands.objects.SubCommand;
import eu.iteije.serverselector.common.core.storage.StorageKey;
import eu.iteije.serverselector.common.messaging.enums.MessageType;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.commands.subcommands.arguments.ArgumentHandler;
import eu.iteije.serverselector.spigot.files.SpigotFile;
import eu.iteije.serverselector.spigot.files.SpigotFileModule;
import eu.iteije.serverselector.spigot.files.SpigotFolder;
import eu.iteije.serverselector.spigot.messaging.SpigotMessageModule;
import eu.iteije.serverselector.spigot.players.ServerSelectorPlayer;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.UUID;

public class AdminReloadArgument extends ArgumentHandler {

    private ServerSelectorSpigot instance;

    public AdminReloadArgument(SubCommand subCommand, String argument, ServerSelectorSpigot instance) {
        super(subCommand, argument);
        this.instance = instance;
    }

    @Override
    public void onExecute(CommonExecutor executor, String[] args) {
        SpigotMessageModule messageModule = instance.getMessageModule();

        CommandSender sender = executor.getSender();

        messageModule.send(StorageKey.RELOAD_STARTED, sender, MessageType.MESSAGE);

        // Reload local files and cache menus
        Collection<SpigotFolder> folders = SpigotFileModule.folders.values();
        SpigotFileModule.folders = null;
        folders.forEach(folder -> {
            new SpigotFolder(instance, folder.getStorageLocation());
        });

        Collection<SpigotFile> files = SpigotFileModule.files.values();
        SpigotFileModule.files = null;
        files.forEach(file -> {
            new SpigotFile(instance, file.getFileName());
        });

        instance.getSelectorModule().cacheMenus();

        messageModule.send(StorageKey.RELOAD_FINISHED_LOCAL, sender, MessageType.MESSAGE);

        // Clear players and register
        ServerSelectorPlayer[] players = instance.getPlayerModule().getPlayers();
        for (ServerSelectorPlayer player : players) {
            UUID uuid = player.getBukkitPlayer().getUniqueId();
            instance.getPlayerModule().removePlayer(uuid);
            instance.getPlayerModule().registerPlayer(uuid);
        }

        messageModule.send(StorageKey.RELOAD_FINISHED_PLAYERS, sender, MessageType.MESSAGE);

        messageModule.send(StorageKey.RELOAD_FINISHED, sender, MessageType.MESSAGE);

        // TODO: file sync
    }
}
