package eu.iteije.serverselector.spigot.metrics;

import lombok.Getter;
import lombok.Setter;

public class SpigotMetrics {

    private Integer redisCalls;

    @Getter @Setter private Boolean enabled;

    public SpigotMetrics() {
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
