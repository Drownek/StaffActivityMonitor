package me.drownek.staffactivity.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.bukkit.scheduler.PlatformScheduler;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.config.PluginConfig;
import me.drownek.staffactivity.data.activity.ActivityPlayer;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;
import me.drownek.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Component
public class ActivityTopGui {

    private @Inject ActivityPlayerRepository repository;
    private @Inject PluginConfig config;
    private @Inject PlatformScheduler scheduler;
    private @Inject ActivityPlayerService activityPlayerService;

    public void openActivityTopGui(HumanEntity player) {
        openActivityTopGui(player, Comparator.comparing(activityPlayer -> activityPlayerService.getPlayerTotalActivityTime(activityPlayer), Comparator.reverseOrder()));
    }

    public void openActivityTopGui(HumanEntity player, Comparator<ActivityPlayer> comparator) {
        scheduler.runAsync(() -> {
            Collection<ActivityPlayer> activitiesSorted = repository.findAll().stream()
                .sorted(comparator)
                .toList();

            PaginatedGui gui = config.viewGuiTop.toPaginatedGuiBuilder().disableAllInteractions().create();

            for (ActivityPlayer activityPlayer : activitiesSorted) {
                UUID playerUuid = activityPlayer.getUuid();
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUuid);
                String name = Optional.ofNullable(offlinePlayer.getName()).orElse(playerUuid.toString());

                ItemStack viewGuiListItem = config.viewGuiTopItem
                    .with("{player}", name)
                    .with("{total_time}", TimeUtil.formatDuration(activityPlayerService.getPlayerTotalActivityTime(activityPlayer)))
                    .with("{total_logins}", activityPlayer.getEntries().size())
                    .getItemStack();

                gui.addItem(ItemBuilder.skull(viewGuiListItem).owner(offlinePlayer).asGuiItem());
            }
            scheduler.runSync(() -> gui.open(player));
        });
    }
}
