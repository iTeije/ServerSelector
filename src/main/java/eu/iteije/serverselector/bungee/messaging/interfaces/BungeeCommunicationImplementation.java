package eu.iteije.serverselector.bungee.messaging.interfaces;

import net.md_5.bungee.api.connection.Connection;

import java.io.DataInputStream;

public interface BungeeCommunicationImplementation {

    void process(DataInputStream input, Connection connection);

}
