package eu.iteije.serverselector.bungee.metrics;

import lombok.Getter;
import lombok.Setter;

public class BungeeMetrics {

    private Integer redisCalls;
    private Integer spigotRedisCalls;

    @Getter @Setter private Boolean enabled;

    public BungeeMetrics() {
        this.enabled = false;

        this.redisCalls = 0;
        this.spigotRedisCalls = 0;
    }

    public void redisCall() {
        redisCalls++;
    }

    public int getRedisCalls() {
        int back = redisCalls;
        redisCalls = 0;
        return back;
    }

    public void spigotRedisCall(Integer calls) {
        spigotRedisCalls += calls;
    }

    public int getSpigotRedisCalls() {
        int back = spigotRedisCalls;
        spigotRedisCalls = 0;
        return back;
    }

}
