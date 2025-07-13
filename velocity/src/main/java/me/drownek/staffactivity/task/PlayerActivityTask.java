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

    /**
     * Periodically updates staff activity records by ensuring all online players with staff permission have an activity record,
     * and processes all tracked players to start or end activity entries based on their current status and permissions.
     */
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

    /**
     * Processes an activity player by updating their activity entry based on their current online status and staff permission.
     *
     * If the player is online and has the required staff permission, starts a new activity entry if none is ongoing.
     * If the player is offline or lacks the required permission, completes any ongoing activity entry.
     *
     * @param user the activity player to process
     */
    private void processPlayer(ActivityPlayer user) {
        ActivityEntry uncompletedEntry = activityPlayerService.getUncompletedActivityEntry(user).orElse(null);

        var player = proxy.getPlayer(user.getUuid()).orElse(null);

        if (player != null && player.hasPermission(config.staffPermission)) {
            handleOnlinePlayer(user, uncompletedEntry);
        } else {
            handleOfflineOrUnauthorizedPlayer(user, uncompletedEntry);
        }
    }

    /**
     * Starts a new activity entry for the player if none is currently ongoing.
     *
     * If the player does not have an uncompleted activity entry, creates a new entry with the current timestamp and saves the user's activity data.
     *
     * @param user The activity player whose activity is being tracked.
     * @param uncompletedEntry The player's current uncompleted activity entry, or null if none exists.
     */
    private void handleOnlinePlayer(ActivityPlayer user, ActivityEntry uncompletedEntry) {
        if (uncompletedEntry == null) {
            user.getEntries().add(new ActivityEntry(Instant.now()));
            user.save();
        }
    }

    /**
     * Completes the ongoing activity entry for a player who is offline or no longer has the required permission.
     *
     * If the player has an uncompleted activity entry, sets its end time to the current instant and saves the updated user data.
     */
    private void handleOfflineOrUnauthorizedPlayer(ActivityPlayer user, ActivityEntry uncompletedEntry) {
        if (uncompletedEntry != null) {
            uncompletedEntry.setEndTime(Instant.now());
            user.save();
        }
    }
}
