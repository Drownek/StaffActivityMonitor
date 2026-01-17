package me.drownek.staffactivity.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.bukkit.scheduler.PlatformScheduler;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.config.Messages;
import me.drownek.staffactivity.config.PluginConfig;
import me.drownek.staffactivity.core.report.PlayerActivityReport;
import me.drownek.staffactivity.core.report.TimePeriod;
import me.drownek.staffactivity.service.ReportService;
import me.drownek.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Component
public class ActivityReportGui {

    private @Inject ReportService reportService;
    private @Inject PluginConfig config;
    private @Inject Messages messages;
    private @Inject PlatformScheduler scheduler;

    public void openTimePeriodSelector(HumanEntity player) {
        Gui gui = config.reportGuiSelector.toGuiBuilder().disableAllInteractions().create();

        int slot = 10;
        for (TimePeriod period : TimePeriod.values()) {
            GuiItem item = config.reportGuiSelectorItem
                    .with("{period}", messages.getTimePeriodDisplayName(period))
                    .asGuiItem(event -> openReportGui(player, period));
            gui.setItem(slot, item);
            slot++;
            if (slot == 13) slot = 14;
        }

        gui.open(player);
    }

    public void openReportGui(HumanEntity player, TimePeriod period) {
        scheduler.runAsync(() -> {
            List<PlayerActivityReport> reports = reportService.generateReports(period);

            PaginatedGui gui = config.reportGui
                    .closeAction(this::openTimePeriodSelector)
                    .toPaginatedGuiBuilder()
                    .disableAllInteractions()
                    .create();

            int rank = 1;
            for (PlayerActivityReport report : reports) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(report.getPlayerUuid());

                ItemStack item = config.reportGuiItem
                        .with("{rank}", rank)
                        .with("{player}", report.getPlayerName())
                        .with("{time}", TimeUtil.formatDuration(report.getTotalActivityTime()))
                        .with("{sessions}", report.getSessionCount())
                        .with("{commands}", report.getCommandCount())
                        .with("{messages}", report.getMessageCount())
                        .with("{period}", messages.getTimePeriodDisplayName(period))
                        .getItemStack();

                gui.addItem(ItemBuilder.skull(item).owner(offlinePlayer).asGuiItem());
                rank++;
            }

            scheduler.runSync(() -> gui.open(player));
        });
    }
}
