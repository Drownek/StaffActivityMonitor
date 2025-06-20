package me.drownek.staffactivity.data.activity;

import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.config.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
public class ActivityPlayerService {

    private @Inject PluginConfig config;
    private @Inject ActivityPlayerRepository repository;

    public Optional<ActivityEntry> getUncompletedActivityEntry(ActivityPlayer activityPlayer) {
        return activityPlayer.getEntries().stream()
            .filter(activityEntry -> activityEntry.getEndTime() == null)
            .findFirst();
    }

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

    public Duration getPlayerTotalActivityTime(ActivityPlayer activityPlayer) {
        return activityPlayer.getEntries().stream()
            .filter(entry -> entry.getEndTime() != null)
            .map(entry -> Duration.between(entry.getStartTime(), entry.getEndTime()))
            .reduce(Duration.ZERO, Duration::plus);
    }
}
