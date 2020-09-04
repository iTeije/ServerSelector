package eu.iteije.serverselector.bungee.metrics;

import lombok.Getter;
import lombok.Setter;

public class BungeeMetrics {

    private Integer redisCalls;

    @Getter @Setter private Boolean enabled;

    public BungeeMetrics() {
        this.enabled = false;

        this.redisCalls = 0;
    }

    public void redisCall() {
        redisCalls++;
    }

    public int getRedisCalls() {
        int back = redisCalls;
        redisCalls = 0;
        return back;
    }

}
