package me.drownek.staffactivity.config;

import com.cryptomorin.xseries.XMaterial;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.drownek.platform.core.annotation.Configuration;
import me.drownek.util.DataItemStack;
import me.drownek.util.ItemStackBuilder;
import me.drownek.util.gui.GuiItemInfo;
import me.drownek.util.gui.GuiSettings;
import me.drownek.util.gui.PaginatedGuiSettings;
import org.bukkit.Material;

import java.util.List;

@SuppressWarnings("CanBeFinal")
@Configuration
public class PluginConfig extends OkaeriConfig {

    @Comment("Permission required to record activity")
    public String staffPermission = "staffactivity.staff";

    @Comment("View gui list settings (/staffactivity view)")
    public PaginatedGuiSettings viewGuiList = PaginatedGuiSettings.builder()
        .title("Staff activity")
        .build();

    public DataItemStack viewGuiListItem = new DataItemStack(ItemStackBuilder.of(XMaterial.PLAYER_HEAD)
        .name("&7Player: &f{player}")
        .lore(List.of("&eClick to view activity"))
        .asItemStack());

    @Comment("Activity view for specific player gui settings")
    public PaginatedGuiSettings viewGuiPlayer = PaginatedGuiSettings.builder()
        .title("Last user activity")
        .build();

    public DataItemStack viewGuiPlayerListItem = new DataItemStack(XMaterial.CLOCK.parseMaterial(), "&7Activity", List.of(
        "&7From: &f{start}",
        "&7To: &f{end}",
        "&7Executed &f{executed_commands} &7commands",
        "&7Sent &f{sent_messages} &7messages",
        "&8► &7Click to view more"
    ));

    public GuiItemInfo viewGuiPlayerSortItem = new GuiItemInfo(43, XMaterial.HOPPER, "&fSort", List.of(
        "{oldest_to_newest_selection} Oldest to newest",
        "{newest_to_oldest_selection} Newest to oldest"
    ));
    public String viewGuiPlayerSortSelected = "&e►";
    public String viewGuiPlayerSortUnselected = "&8-&f";

    @Comment("Viewing contents of commands/messages after clicking view more in player list item")
    public PaginatedGuiSettings viewGuiPlayerMore = PaginatedGuiSettings.builder()
        .title("User activity details")
        .build();

    public DataItemStack viewGuiPlayerMoreMessageSent = new DataItemStack(Material.PAPER, "&7Message sent", List.of("&7Time: &f{TIME}", "&f{ACTION}"));
    public DataItemStack viewGuiPlayerMoreCommandSent = new DataItemStack(Material.MAP, "&7Command sent", List.of("&7Time: &f{TIME}", "&f{ACTION}"));

    @Comment("Activity report GUI - Time period selector (/staffactivity report)")
    public GuiSettings reportGuiSelector = GuiSettings.builder()
        .title("Select Time Period")
        .rows(3)
        .build();

    public DataItemStack reportGuiSelectorItem = new DataItemStack(XMaterial.CLOCK, "&e{period}", List.of(
        "&7Click to view report for this period"
    ));

    @Comment("Activity report GUI - Report view")
    public PaginatedGuiSettings reportGui = PaginatedGuiSettings.builder()
        .title("Activity Report")
        .build();

    public DataItemStack reportGuiItem = new DataItemStack(ItemStackBuilder.of(XMaterial.PLAYER_HEAD)
        .name("&e#{rank} &f{player}")
        .lore(List.of(
            "&7Period: &f{period}",
            "&7",
            "&7Time online: &f{time}",
            "&7Sessions: &f{sessions}",
            "&7Commands: &f{commands}",
            "&7Messages: &f{messages}"
        ))
        .asItemStack());

    @Comment("PlaceholderAPI configuration")
    public PlaceholderConfig placeholders = new PlaceholderConfig();

    @Comment({"Whether to use proxy mode or not", "NOTE: When true, storageType must be set to MYSQL"})
    public boolean proxyMode = false;

    @Comment("Database configuration")
    public StorageConfig storage = new StorageConfig();

    public static class PlaceholderConfig extends OkaeriConfig {

        @Comment("Time format for placeholders: {days}d {hours}h {minutes}m")
        public String timeFormat = "{days}d {hours}h {minutes}m";

        @Comment("Date format for last_seen placeholder (Java DateTimeFormatter pattern)")
        public String lastSeenFormat = "yyyy-MM-dd HH:mm:ss";

        @Comment("Value to display when player is not tracked by staff activity")
        public String notTrackedValue = "N/A";
    }

    public static class StorageConfig extends OkaeriConfig {

        @Comment("Type of the storage backend: FLAT, MYSQL, POSTGRES")
        public StorageType backend = StorageType.FLAT;

        @Comment("Prefix for the storage")
        public String prefix = "StaffActivityMonitor";

        @Comment("Database host")
        public String host = "localhost";

        @Comment("Database port (default 3306 for MySQL, 5432 for PostgreSQL)")
        public int port = 3306;

        @Comment("Database name")
        public String database = "db";

        public String user = "";

        public String password = "";
    }
}
