package eu.iteije.serverselector.spigot.messaging.requests;

import eu.iteije.serverselector.common.messaging.enums.MessageChannel;
import eu.iteije.serverselector.spigot.ServerSelectorSpigot;
import eu.iteije.serverselector.spigot.messaging.interfaces.SpigotRequestImplementation;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UpdateMessageRequest implements SpigotRequestImplementation {

    /**
     * A separate request is necessary for updating messages, since the bungee
     *  has no command for updating messages and the broadcast won't execute the
     *  command on behalf of the bungee server in first place.
     */

    private ServerSelectorSpigot instance;

    public UpdateMessageRequest(ServerSelectorSpigot instance) {
        this.instance = instance;
    }

    @Override
    public void process(String... arguments) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);

        try {
            // Set the request type to UpdateMessage
            outputStream.writeUTF("UpdateMessage");
            // Set the path of the message
            outputStream.writeUTF(arguments[0]);
            // Set the updated message
            outputStream.writeUTF(arguments[1]);

            // Send the message
            instance.getServer().sendPluginMessage(instance, MessageChannel.BUNGEE_GLOBAL.getChannel(), stream.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        }


    }

}
