package eu.iteije.serverselector.common.core.logging;

public class ServerSelectorLogger {

    private static final String LOG_PREFIX = "[ServerSelector] ";

    public static void console(String message) {
        System.out.println(LOG_PREFIX + message);
    }

    public static void console(String message, Exception exception) {
        System.out.println(LOG_PREFIX + message + " (" + exception.getMessage() + ")");
    }

}
