package me.drownek.staffactivity.core.report;

import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.UUID;

@Getter
@Builder
public class PlayerActivityReport {
    private final UUID playerUuid;
    private final String playerName;
    private final Duration totalActivityTime;
    private final int sessionCount;
    private final long commandCount;
    private final long messageCount;
    private final TimePeriod timePeriod;

    public long getTotalMinutes() {
        return totalActivityTime.toMinutes();
    }
}
