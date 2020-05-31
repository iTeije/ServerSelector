package eu.iteije.serverselector.bungee.messaging.interfaces;

import java.io.DataInputStream;

public interface BungeeHandlerImplementation {

    void process(DataInputStream input, String sender);

}
