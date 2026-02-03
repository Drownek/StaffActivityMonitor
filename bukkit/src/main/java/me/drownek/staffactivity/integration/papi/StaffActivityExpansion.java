package me.drownek.staffactivity.integration.papi;

import eu.okaeri.injector.annotation.Inject;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.drownek.staffactivity.config.PluginConfig;
import me.drownek.staffactivity.core.ActivityPlayer;
import me.drownek.staffactivity.core.action.ActionType;
import me.drownek.staffactivity.core.report.PlayerActivityReport;
import me.drownek.staffactivity.core.report.TimePeriod;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;
import me.drownek.staffactivity.service.ReportService;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class StaffActivityExpansion extends PlaceholderExpansion {

    private @Inject PluginConfig config;
    private @Inject ActivityPlayerRepository repository;
    private @Inject ActivityPlayerService service;
    private @Inject ReportService reportService;

    @Override
    public @NotNull String getIdentifier() {
        return "staffactivity";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Drownek";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null || !player.isOnline()) {
            return config.placeholders.notTrackedValue;
        }

        Player onlinePlayer = player.getPlayer();
        if (onlinePlayer == null || !onlinePlayer.hasPermission(config.staffPermission)) {
            return config.placeholders.notTrackedValue;
        }

        ActivityPlayer activityPlayer = repository.getUser(onlinePlayer);

        return switch (params.toLowerCase()) {
            case "total_time" -> formatDuration(service.getPlayerTotalActivityTime(activityPlayer));
            case "daily_time" -> formatDuration(getTimeForPeriod(activityPlayer, TimePeriod.TODAY));
            case "weekly_time" -> formatDuration(getTimeForPeriod(activityPlayer, TimePeriod.LAST_7_DAYS));
            case "monthly_time" -> formatDuration(getTimeForPeriod(activityPlayer, TimePeriod.LAST_30_DAYS));
            case "yearly_time" -> formatDuration(getTimeForPeriod(activityPlayer, TimePeriod.THIS_MONTH));
            
            case "session_count" -> String.valueOf(getTotalSessionCount(activityPlayer));
            case "daily_session_count" -> String.valueOf(getSessionCountForPeriod(activityPlayer, TimePeriod.TODAY));
            case "weekly_session_count" -> String.valueOf(getSessionCountForPeriod(activityPlayer, TimePeriod.LAST_7_DAYS));
            case "monthly_session_count" -> String.valueOf(getSessionCountForPeriod(activityPlayer, TimePeriod.LAST_30_DAYS));
            
            case "commands_count" -> String.valueOf(getTotalCommandsCount(activityPlayer));
            case "daily_commands_count" -> String.valueOf(getCommandsCountForPeriod(activityPlayer, TimePeriod.TODAY));
            case "weekly_commands_count" -> String.valueOf(getCommandsCountForPeriod(activityPlayer, TimePeriod.LAST_7_DAYS));
            case "monthly_commands_count" -> String.valueOf(getCommandsCountForPeriod(activityPlayer, TimePeriod.LAST_30_DAYS));
            
            case "messages_count" -> String.valueOf(getTotalMessagesCount(activityPlayer));
            case "daily_messages_count" -> String.valueOf(getMessagesCountForPeriod(activityPlayer, TimePeriod.TODAY));
            case "weekly_messages_count" -> String.valueOf(getMessagesCountForPeriod(activityPlayer, TimePeriod.LAST_7_DAYS));
            case "monthly_messages_count" -> String.valueOf(getMessagesCountForPeriod(activityPlayer, TimePeriod.LAST_30_DAYS));
            
            case "last_seen" -> getLastSeen(activityPlayer);
            case "average_session" -> formatDuration(getAverageSessionDuration(activityPlayer));
            
            case "rank" -> String.valueOf(getRank(activityPlayer, TimePeriod.ALL_TIME));
            case "daily_rank" -> String.valueOf(getRank(activityPlayer, TimePeriod.TODAY));
            case "weekly_rank" -> String.valueOf(getRank(activityPlayer, TimePeriod.LAST_7_DAYS));
            case "monthly_rank" -> String.valueOf(getRank(activityPlayer, TimePeriod.LAST_30_DAYS));
            
            default -> config.placeholders.notTrackedValue;
        };
    }

    private Duration getTimeForPeriod(ActivityPlayer activityPlayer, TimePeriod period) {
        Instant startInstant = period.getStartInstant();
        Instant endInstant = period.getEndInstant();

        return activityPlayer.getEntries().stream()
                .filter(entry -> entry.getEndTime() != null)
                .filter(entry -> !entry.getStartTime().isBefore(startInstant) && entry.getStartTime().isBefore(endInstant))
                .map(entry -> Duration.between(entry.getStartTime(), entry.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    private String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;

        return config.placeholders.timeFormat
                .replace("{days}", String.valueOf(days))
                .replace("{hours}", String.valueOf(hours))
                .replace("{minutes}", String.valueOf(minutes));
    }

    private long getTotalSessionCount(ActivityPlayer activityPlayer) {
        return activityPlayer.getEntries().stream()
                .filter(entry -> entry.getEndTime() != null)
                .count();
    }

    private long getSessionCountForPeriod(ActivityPlayer activityPlayer, TimePeriod period) {
        Instant startInstant = period.getStartInstant();
        Instant endInstant = period.getEndInstant();

        return activityPlayer.getEntries().stream()
                .filter(entry -> entry.getEndTime() != null)
                .filter(entry -> !entry.getStartTime().isBefore(startInstant) && entry.getStartTime().isBefore(endInstant))
                .count();
    }

    private long getTotalCommandsCount(ActivityPlayer activityPlayer) {
        return activityPlayer.getEntries().stream()
                .flatMap(entry -> entry.getActions().values().stream())
                .filter(action -> action.getType() == ActionType.COMMAND)
                .count();
    }

    private long getCommandsCountForPeriod(ActivityPlayer activityPlayer, TimePeriod period) {
        Instant startInstant = period.getStartInstant();
        Instant endInstant = period.getEndInstant();

        return activityPlayer.getEntries().stream()
                .flatMap(entry -> entry.getActions().values().stream())
                .filter(action -> action.getType() == ActionType.COMMAND)
                .filter(action -> !action.getTime().isBefore(startInstant) && action.getTime().isBefore(endInstant))
                .count();
    }

    private long getTotalMessagesCount(ActivityPlayer activityPlayer) {
        return activityPlayer.getEntries().stream()
                .flatMap(entry -> entry.getActions().values().stream())
                .filter(action -> action.getType() == ActionType.MESSAGE)
                .count();
    }

    private long getMessagesCountForPeriod(ActivityPlayer activityPlayer, TimePeriod period) {
        Instant startInstant = period.getStartInstant();
        Instant endInstant = period.getEndInstant();

        return activityPlayer.getEntries().stream()
                .flatMap(entry -> entry.getActions().values().stream())
                .filter(action -> action.getType() == ActionType.MESSAGE)
                .filter(action -> !action.getTime().isBefore(startInstant) && action.getTime().isBefore(endInstant))
                .count();
    }

    private String getLastSeen(ActivityPlayer activityPlayer) {
        return activityPlayer.getEntries().stream()
                .map(activityEntry -> Optional.ofNullable(activityEntry.getEndTime()).orElse(Instant.now()))
                .max(Instant::compareTo)
                .map(instant -> DateTimeFormatter.ofPattern(config.placeholders.lastSeenFormat)
                        .withZone(ZoneId.systemDefault())
                        .format(instant))
                .orElse(config.placeholders.notTrackedValue);
    }

    private Duration getAverageSessionDuration(ActivityPlayer activityPlayer) {
        List<Duration> durations = activityPlayer.getEntries().stream()
                .map(entry -> {
                    Instant endTime = Optional.ofNullable(entry.getEndTime()).orElse(Instant.now());
                    return Duration.between(entry.getStartTime(), endTime);
                })
                .toList();

        if (durations.isEmpty()) {
            return Duration.ZERO;
        }

        long totalSeconds = durations.stream().mapToLong(Duration::getSeconds).sum();
        return Duration.ofSeconds(totalSeconds / durations.size());
    }

    private int getRank(ActivityPlayer activityPlayer, TimePeriod period) {
        List<PlayerActivityReport> reports = reportService.generateReports(period);
        
        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getPlayerUuid().equals(activityPlayer.getUuid())) {
                return i + 1;
            }
        }
        
        return reports.size() + 1;
    }
}
