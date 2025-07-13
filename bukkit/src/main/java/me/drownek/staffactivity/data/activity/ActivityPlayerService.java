package me.drownek.staffactivity.data.activity;

import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.config.PluginConfig;
import me.drownek.staffactivity.core.ActivityEntry;
import me.drownek.staffactivity.core.ActivityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
public class ActivityPlayerService {

    private @Inject PluginConfig config;
    private @Inject ActivityPlayerRepository repository;

    /**
     * Returns the first uncompleted activity entry for the specified player, if one exists.
     *
     * An uncompleted activity entry is defined as one with a null end time.
     *
     * @param activityPlayer the player whose activity entries are to be checked
     * @return an {@code Optional} containing the first uncompleted {@code ActivityEntry}, or empty if none exist
     */
    public Optional<ActivityEntry> getUncompletedActivityEntry(ActivityPlayer activityPlayer) {
        return activityPlayer.getEntries().stream()
                .filter(activityEntry -> activityEntry.getEndTime() == null)
                .findFirst();
    }

    /**
     * Completes all active activity entries for online players with staff permissions by setting their end time to the current instant.
     *
     * For each online player with the configured staff permission, this method finds any uncompleted activity entry and marks it as completed.
     */
    public void completeAllActiveEntries() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(config.staffPermission)) {
                ActivityPlayer user = repository.getUser(player);
                ActivityEntry uncompletedEntry = getUncompletedActivityEntry(user).orElse(null);

                if (uncompletedEntry != null) {
                    uncompletedEntry.setEndTime(Instant.now());
                    user.save();
                }
            }
        }
    }

    /**
     * Calculates the total duration of all completed activity entries for the specified player.
     *
     * Only entries with a non-null end time are included in the total.
     *
     * @param activityPlayer the player whose completed activity durations are summed
     * @return the total duration of all completed activity entries
     */
    public Duration getPlayerTotalActivityTime(ActivityPlayer activityPlayer) {
        return activityPlayer.getEntries().stream()
                .filter(entry -> entry.getEndTime() != null)
                .map(entry -> Duration.between(entry.getStartTime(), entry.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);
    }
}
