package eu.iteije.serverselector.spigot.runnables;

import eu.iteije.serverselector.spigot.ServerSelectorSpigot;

import java.util.LinkedList;

public class ServerSelectorTimer implements Runnable {

    private ServerSelectorSpigot instance;
    private long last = System.nanoTime();
    private final LinkedList<Double> history = new LinkedList<>();
    private int skip1 = 0;
    private int skip2 = 0;
    private final long maxTime = 10 * 1000000;
    private final long tickInterval = 50;

    public ServerSelectorTimer(ServerSelectorSpigot instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        final long start = System.nanoTime();
        final long current = System.currentTimeMillis();

        long spent = (start - last) / 1000;

        if (spent == 0) spent = 1;

        if (history.size() > 10) history.remove();

        double tps = tickInterval * 1000000.0 / spent;

        if (tps <= 21) history.add(tps);



    }
}
