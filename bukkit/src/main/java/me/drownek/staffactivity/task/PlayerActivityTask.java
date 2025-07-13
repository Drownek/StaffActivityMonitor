package me.drownek.staffactivity.task;

import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.bukkit.annotation.Scheduled;
import me.drownek.platform.bukkit.scheduler.PlatformScheduler;
import me.drownek.staffactivity.config.PluginConfig;
import me.drownek.staffactivity.core.ActivityEntry;
import me.drownek.staffactivity.core.ActivityPlayer;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Instant;

/**
 * Using task instead of listeners to handle the situation where the player was given a permission
 * after joining the server to record activity from moment he got the permission,
 * not at his second join.
 */
@Scheduled(rate = 20, async = true)
public class PlayerActivityTask implements Runnable {

    private @Inject ActivityPlayerRepository repository;
    private @Inject ActivityPlayerService activityPlayerService;
    private @Inject PluginConfig config;
    private @Inject PlatformScheduler scheduler;

    /**
     * Executes the scheduled task to process activity tracking for all offline players,
     * unless the plugin is running in proxy mode.
     */
    @Override
    public void run() {
        if (config.proxyMode) {
            return;
        }

        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            processPlayer(offlinePlayer);
        }
    }

    private void processPlayer(OfflinePlayer offlinePlayer) {
        ActivityPlayer user = repository.getUser(offlinePlayer);
        ActivityEntry uncompletedEntry = activityPlayerService.getUncompletedActivityEntry(user).orElse(null);
        Player player = offlinePlayer.getPlayer();

        if (player != null && player.hasPermission(config.staffPermission)) {
            handleOnlinePlayer(user, uncompletedEntry);
        } else {
            handleOfflineOrUnauthorizedPlayer(user, uncompletedEntry);
        }
    }

    private void handleOnlinePlayer(ActivityPlayer user, ActivityEntry uncompletedEntry) {
        if (uncompletedEntry == null) {
            user.getEntries().add(new ActivityEntry(Instant.now()));
            user.save();
        }
    }

    private void handleOfflineOrUnauthorizedPlayer(ActivityPlayer user, ActivityEntry uncompletedEntry) {
        if (uncompletedEntry != null) {
            uncompletedEntry.setEndTime(Instant.now());
            user.save();
        }
    }
}
