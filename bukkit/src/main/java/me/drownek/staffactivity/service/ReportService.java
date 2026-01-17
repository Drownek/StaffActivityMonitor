package me.drownek.staffactivity.service;

import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.config.Messages;
import me.drownek.staffactivity.core.ActivityEntry;
import me.drownek.staffactivity.core.ActivityPlayer;
import me.drownek.staffactivity.core.action.Action;
import me.drownek.staffactivity.core.action.ActionType;
import me.drownek.staffactivity.core.report.PlayerActivityReport;
import me.drownek.staffactivity.core.report.TimePeriod;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class ReportService {

    private @Inject ActivityPlayerRepository repository;
    private @Inject Messages messages;

    public List<PlayerActivityReport> generateReports(TimePeriod period) {
        Collection<ActivityPlayer> allPlayers = repository.findAll();
        List<PlayerActivityReport> reports = new ArrayList<>();

        Instant periodStart = period.getStartInstant();
        Instant periodEnd = period.getEndInstant();

        for (ActivityPlayer activityPlayer : allPlayers) {
            PlayerActivityReport report = generateReportForPlayer(activityPlayer, period, periodStart, periodEnd);
            if (report.getSessionCount() > 0 || period == TimePeriod.ALL_TIME) {
                reports.add(report);
            }
        }

        reports.sort(Comparator.comparing(PlayerActivityReport::getTotalActivityTime).reversed());
        return reports;
    }

    public PlayerActivityReport generateReportForPlayer(ActivityPlayer activityPlayer, TimePeriod period) {
        return generateReportForPlayer(activityPlayer, period, period.getStartInstant(), period.getEndInstant());
    }

    private PlayerActivityReport generateReportForPlayer(ActivityPlayer activityPlayer, TimePeriod period, Instant periodStart, Instant periodEnd) {
        UUID playerUuid = activityPlayer.getUuid();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUuid);
        String playerName = Optional.ofNullable(offlinePlayer.getName()).orElse(playerUuid.toString());

        Duration totalTime = Duration.ZERO;
        int sessionCount = 0;
        long commandCount = 0;
        long messageCount = 0;

        for (ActivityEntry entry : activityPlayer.getEntries()) {
            Instant entryStart = entry.getStartTime();
            Instant entryEnd = entry.getEndTime() != null ? entry.getEndTime() : Instant.now();

            if (entryEnd.isBefore(periodStart) || entryStart.isAfter(periodEnd)) {
                continue;
            }

            Instant effectiveStart = entryStart.isBefore(periodStart) ? periodStart : entryStart;
            Instant effectiveEnd = entryEnd.isAfter(periodEnd) ? periodEnd : entryEnd;

            totalTime = totalTime.plus(Duration.between(effectiveStart, effectiveEnd));
            sessionCount++;

            for (Action action : entry.getActions().values()) {
                Instant actionTime = action.getTime();
                if (actionTime.isAfter(periodStart) && actionTime.isBefore(periodEnd)) {
                    if (action.getType() == ActionType.COMMAND) {
                        commandCount++;
                    } else if (action.getType() == ActionType.MESSAGE) {
                        messageCount++;
                    }
                }
            }
        }

        return PlayerActivityReport.builder()
                .playerUuid(playerUuid)
                .playerName(playerName)
                .totalActivityTime(totalTime)
                .sessionCount(sessionCount)
                .commandCount(commandCount)
                .messageCount(messageCount)
                .timePeriod(period)
                .build();
    }

    public File exportToCsv(List<PlayerActivityReport> reports, File pluginFolder) throws IOException {
        File exportsFolder = new File(pluginFolder, "exports");
        if (!exportsFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            exportsFolder.mkdirs();
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        TimePeriod period = reports.isEmpty() ? TimePeriod.ALL_TIME : reports.get(0).getTimePeriod();
        String fileName = "activity_report_" + period.name().toLowerCase() + "_" + timestamp + ".csv";
        File csvFile = new File(exportsFolder, fileName);

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            writer.println("Player Name,Player UUID,Total Time (minutes),Sessions,Commands,Messages,Period");

            for (PlayerActivityReport report : reports) {
                writer.printf("%s,%s,%d,%d,%d,%d,%s%n",
                        escapeCsv(report.getPlayerName()),
                        report.getPlayerUuid().toString(),
                        report.getTotalMinutes(),
                        report.getSessionCount(),
                        report.getCommandCount(),
                        report.getMessageCount(),
                        messages.getTimePeriodDisplayName(report.getTimePeriod())
                );
            }
        }

        return csvFile;
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
