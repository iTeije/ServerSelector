package eu.iteije.serverselector.common.commands.interfaces;

public interface CommonExecutor {

    boolean hasPermission(String permission);
    Object getSender();

}
