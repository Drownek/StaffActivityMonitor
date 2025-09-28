package me.drownek.staffactivity.task;

import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.bungee.annotation.Scheduled;
import me.drownek.staffactivity.PluginConfig;
import me.drownek.staffactivity.core.ActivityEntry;
import me.drownek.staffactivity.core.ActivityPlayer;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
    private @Inject ProxyServer proxy;

    @Override
    public void run() {
        for (ProxiedPlayer player : proxy.getPlayers()) {
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

        var player = proxy.getPlayer(user.getUuid());

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
