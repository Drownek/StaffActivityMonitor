package me.drownek.staffactivity.listener;

import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.bukkit.scheduler.PlatformScheduler;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.config.PluginConfig;
import me.drownek.staffactivity.core.ActivityPlayer;
import me.drownek.staffactivity.core.action.Action;
import me.drownek.staffactivity.core.action.ActionType;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.time.Instant;

@Component
public class PlayerCommandListener implements Listener {

    private @Inject PluginConfig config;
    private @Inject ActivityPlayerRepository repository;
    private @Inject PlatformScheduler scheduler;
    private @Inject ActivityPlayerService activityPlayerService;

    @EventHandler(priority = EventPriority.MONITOR)
    void handle(PlayerCommandPreprocessEvent event) {
        if (config.proxyMode) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.hasPermission(config.staffPermission)) {
            return;
        }

        scheduler.runAsync(() -> {
            ActivityPlayer user = repository.getUser(player);
            activityPlayerService.getUncompletedActivityEntry(user).ifPresent(activityEntry -> {
                String command = event.getMessage().startsWith("/") ? event.getMessage() : "/" + event.getMessage();
                activityEntry.getActions().put(Instant.now().toEpochMilli(), new Action(ActionType.COMMAND, command));
                user.save();
            });
        });
    }
}
