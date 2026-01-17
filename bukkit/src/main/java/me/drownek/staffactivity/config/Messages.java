package me.drownek.staffactivity.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.drownek.platform.bukkit.commands.LiteCommandsConfig;
import me.drownek.platform.core.annotation.Configuration;
import me.drownek.util.message.SendableMessage;

@SuppressWarnings("CanBeFinal")
@Configuration(path = "messages.{ext}")
public class Messages extends OkaeriConfig {
    public SendableMessage configReloaded = SendableMessage.of("<#67db6c>Config reloaded!");
    public SendableMessage configReloadFail = SendableMessage.of("<#FF415C>Config failed to load, check console errors!");
    public String playerNotFound = "Player not found.";

    @Comment("Report export messages")
    public SendableMessage exportSuccess = SendableMessage.of("<#67db6c>Activity report exported to: <white>{file}");
    public SendableMessage exportFailed = SendableMessage.of("<#FF415C>Failed to export activity report! Check console for details.");

    @Comment("Time period display names")
    public String timePeriodToday = "Today";
    public String timePeriodYesterday = "Yesterday";
    public String timePeriodLast7Days = "Last 7 Days";
    public String timePeriodLast30Days = "Last 30 Days";
    public String timePeriodThisMonth = "This Month";
    public String timePeriodAllTime = "All Time";

    public String getTimePeriodDisplayName(me.drownek.staffactivity.core.report.TimePeriod period) {
        return switch (period) {
            case TODAY -> timePeriodToday;
            case YESTERDAY -> timePeriodYesterday;
            case LAST_7_DAYS -> timePeriodLast7Days;
            case LAST_30_DAYS -> timePeriodLast30Days;
            case THIS_MONTH -> timePeriodThisMonth;
            case ALL_TIME -> timePeriodAllTime;
        };
    }

    @Comment("Time period argument error")
    public String invalidTimePeriod = "Invalid time period: {argument}. Valid options: today, yesterday, last-7-days, last-30-days, this-month, all-time";

    @Comment("Messages for commands library, note that not all of them are used in the plugin!")
    public LiteCommandsConfig liteCommandsConfig = new LiteCommandsConfig();
}
