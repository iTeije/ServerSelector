package eu.iteije.serverselector.spigot.runnables;

import java.util.LinkedList;

public class SelectorTimer implements Runnable {

    private long lastPoll = System.nanoTime();
    private final LinkedList<Double> history = new LinkedList<>();
    private final long tickInterval = 50;

    public SelectorTimer() {
        history.add(20d);
    }


    @Override
    public void run() {
        final long start = System.nanoTime();

        long timeSpent = (start - lastPoll) / 1000;

        if (timeSpent == 0) timeSpent = 1;

        if (history.size() > 10) history.remove();

        double tps = tickInterval * 1000000.0 / timeSpent;

        if (tps <= 21) {
            history.add(tps);
        }

        this.lastPoll = start;
    }

    public double getAverageTPS() {
        double average = 0;

        for (Double item : history) {
            if (item != null) average += item;
        }

        return average / history.size();
    }

}
