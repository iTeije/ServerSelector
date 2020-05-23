package eu.iteije.serverselector.bungee.messaging.interfaces;

import java.io.DataInputStream;

public interface BungeeCommunicationImplementation {

    void process(DataInputStream input, String sender);

}
