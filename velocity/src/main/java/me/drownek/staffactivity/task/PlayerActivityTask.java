package me.drownek.staffactivity.task;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.velocity.annotation.Scheduled;
import eu.okaeri.platform.velocity.scheduler.PlatformScheduler;
import me.drownek.staffactivity.PluginConfig;
import me.drownek.staffactivity.core.ActivityEntry;
import me.drownek.staffactivity.core.ActivityPlayer;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Using task instead of listeners to handle the situation where the player was given a permission
 * after joining the server to record activity from moment he got the permission,
 * not at his second join.
 */
@Scheduled(rate = 1, timeUnit = TimeUnit.SECONDS)
public class PlayerActivityTask implements Runnable {

    private @Inject ActivityPlayerRepository repository;
    private @Inject ActivityPlayerService activityPlayerService;
    private @Inject PluginConfig config;
    private @Inject PlatformScheduler scheduler;
    private @Inject ProxyServer proxy;

    @Override
    public void run() {
        for (Player player : proxy.getAllPlayers()) {
            if (player.hasPermission(config.staffPermission) && !repository.existsByPath(player.getUniqueId())) {
                repository.findOrCreateByPath(player.getUniqueId());
            }
        }

        for (ActivityPlayer user : repository.findAll()) {
            processPlayer(user);
        }
    }

    private void processPlayer(ActivityPlayer user) {
        ActivityEntry uncompletedEntry = activityPlayerService.getUncompletedActivityEntry(user).orElse(null);

        var player = proxy.getPlayer(user.getUuid()).orElse(null);

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
