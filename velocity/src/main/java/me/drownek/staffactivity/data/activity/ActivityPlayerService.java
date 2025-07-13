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

    public Optional<ActivityEntry> getUncompletedActivityEntry(ActivityPlayer activityPlayer) {
        return activityPlayer.getEntries().stream()
            .filter(activityEntry -> activityEntry.getEndTime() == null)
            .findFirst();
    }

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
