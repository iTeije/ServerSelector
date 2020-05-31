package eu.iteije.serverselector.spigot.messaging.requests;

import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.interfaces.SpigotRequestImplementation;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LeaveQueueRequest implements SpigotRequestImplementation {

    private ServerSelectorSpigot instance;

    public LeaveQueueRequest(ServerSelectorSpigot instance) {
        this.instance = instance;
    }

    @Override
    public void process(String... arguments) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);

        try {
            // Set the request type
            outputStream.writeUTF("LeaveQueue");
            // Set player UUID
            outputStream.writeUTF(arguments[0]);

            instance.getServer().sendPluginMessage(instance, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
