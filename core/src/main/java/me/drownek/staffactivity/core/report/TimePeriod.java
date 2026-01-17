package me.drownek.staffactivity.core.report;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public enum TimePeriod {
    TODAY,
    YESTERDAY,
    LAST_7_DAYS,
    LAST_30_DAYS,
    THIS_MONTH,
    ALL_TIME;

    public Instant getStartInstant() {
        LocalDate now = LocalDate.now();
        return switch (this) {
            case TODAY -> now.atStartOfDay(ZoneId.systemDefault()).toInstant();
            case YESTERDAY -> now.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            case LAST_7_DAYS -> now.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant();
            case LAST_30_DAYS -> now.minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant();
            case THIS_MONTH -> now.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            case ALL_TIME -> Instant.EPOCH;
        };
    }

    public Instant getEndInstant() {
        if (this == YESTERDAY) {
            return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        return Instant.now();
    }

    public String getDisplayName() {
        return switch (this) {
            case TODAY -> "Today";
            case YESTERDAY -> "Yesterday";
            case LAST_7_DAYS -> "Last 7 Days";
            case LAST_30_DAYS -> "Last 30 Days";
            case THIS_MONTH -> "This Month";
            case ALL_TIME -> "All Time";
        };
    }
}
