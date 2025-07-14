package me.drownek.staffactivity.config;

import com.cryptomorin.xseries.XMaterial;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.drownek.platform.core.annotation.Configuration;
import me.drownek.util.DataItemStack;
import me.drownek.util.ItemStackBuilder;
import me.drownek.util.gui.GuiItemInfo;
import me.drownek.util.gui.PaginatedGuiSettings;
import org.bukkit.Material;

import java.util.List;

@SuppressWarnings("CanBeFinal")
@Configuration
public class PluginConfig extends OkaeriConfig {

    @Comment("Permission required to record activity")
    public String staffPermission = "staffactivity.staff";

    @Comment("View gui top settings (/staffactivity top)")
    public PaginatedGuiSettings viewGuiTop = PaginatedGuiSettings.builder()
        .title("Top staff activity")
        .build();

    public DataItemStack viewGuiTopItem = new DataItemStack(ItemStackBuilder.of(XMaterial.PLAYER_HEAD)
        .name("&7Player: &f{player}")
        .lore(List.of("&7Total activity time: &f{total_time}", "&7Total server login amount: &f{total_logins}"))
        .asItemStack());

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
        .title("Last user activity")
        .build();

    public DataItemStack viewGuiPlayerMoreMessageSent = new DataItemStack(Material.PAPER, "&7Message sent", List.of("&7Time: &f{TIME}", "&f{ACTION}"));
    public DataItemStack viewGuiPlayerMoreCommandSent = new DataItemStack(Material.MAP, "&7Command sent", List.of("&7Time: &f{TIME}", "&f{ACTION}"));

    @Comment({"Whether to use proxy mode or not", "NOTE: When true, storageType must be set to MYSQL"})
    public boolean proxyMode = false;

    @Comment("Database configuration")
    public StorageConfig storageConfig = new StorageConfig();

    public static class StorageConfig extends OkaeriConfig {
        @Comment("Available types: MYSQL, FLAT")
        public StorageType storageType = StorageType.FLAT;

        public String prefix = "StaffActivityMonitor";

        @Comment("URI if mysql is enabled")
        public String uri = "jdbc:mysql://localhost:3306/mydatabase?user=user123&password=<PASSWORD>";
    }
}
