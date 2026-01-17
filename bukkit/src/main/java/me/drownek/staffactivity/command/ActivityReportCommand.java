package me.drownek.staffactivity.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.bukkit.scheduler.PlatformScheduler;
import me.drownek.staffactivity.config.Messages;
import me.drownek.staffactivity.core.report.PlayerActivityReport;
import me.drownek.staffactivity.core.report.TimePeriod;
import me.drownek.staffactivity.gui.ActivityReportGui;
import me.drownek.staffactivity.service.ReportService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

@Command(name = "staffactivity")
@Permission("staffactivity.commands")
public class ActivityReportCommand {

    private @Inject ActivityReportGui activityReportGui;
    private @Inject ReportService reportService;
    private @Inject PlatformScheduler scheduler;
    private @Inject Plugin plugin;
    private @Inject Messages messages;

    @Execute(name = "report")
    void report(@Context Player player) {
        activityReportGui.openTimePeriodSelector(player);
    }

    @Execute(name = "report")
    void reportPeriod(@Context Player player, @Arg TimePeriod period) {
        activityReportGui.openReportGui(player, period);
    }

    @Execute(name = "export")
    void export(@Context CommandSender sender) {
        exportPeriod(sender, TimePeriod.ALL_TIME);
    }

    @Execute(name = "export")
    void exportPeriod(@Context CommandSender sender, @SuppressWarnings("SameParameterValue") @Arg TimePeriod period) {
        scheduler.runAsync(() -> {
            try {
                List<PlayerActivityReport> reports = reportService.generateReports(period);
                File csvFile = reportService.exportToCsv(reports, plugin.getDataFolder());
                scheduler.runSync(() -> messages.exportSuccess.with("{file}", csvFile.getName()).sendTo(sender));
            } catch (Exception e) {
                scheduler.runSync(() -> messages.exportFailed.sendTo(sender));
                plugin.getLogger().severe("Failed to export activity report: " + e.getMessage());
            }
        });
    }
}
