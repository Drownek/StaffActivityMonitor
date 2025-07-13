package me.drownek.staffactivity.data.activity;

import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.PluginConfig;
import me.drownek.staffactivity.core.ActivityEntry;
import me.drownek.staffactivity.core.ActivityPlayer;

import java.time.Instant;
import java.util.Optional;

@Component
public class ActivityPlayerService {

    private @Inject PluginConfig config;
    private @Inject ActivityPlayerRepository repository;
    private @Inject ProxyServer proxyServer;

    /**
     * Retrieves the first uncompleted activity entry for the specified player.
     *
     * An uncompleted activity entry is one where the end time is not set.
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
     * Completes all active (uncompleted) activity entries for currently connected staff players.
     *
     * Iterates over all connected players with the required staff permission, finds any of their ongoing activity entries,
     * sets the end time to the current instant, and saves the updated player data.
     */
    public void completeAllActiveEntries() {
        proxyServer.getAllPlayers().stream()
            .filter(InboundConnection::isActive)
            .filter(player -> player.hasPermission(config.staffPermission))
            .map(player -> repository.getUser(player))
            .forEach(user -> {
                ActivityEntry uncompletedEntry = getUncompletedActivityEntry(user).orElse(null);
                if (uncompletedEntry != null) {
                    uncompletedEntry.setEndTime(Instant.now());
                    user.save();
                }
            });
    }
}
