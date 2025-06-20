package me.drownek.staffactivity.listener;

import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.bukkit.scheduler.PlatformScheduler;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.config.PluginConfig;
import me.drownek.staffactivity.data.action.Action;
import me.drownek.staffactivity.data.action.ActionType;
import me.drownek.staffactivity.data.activity.ActivityPlayer;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.time.Instant;

@Component
public class PlayerChatListener implements Listener {

    private @Inject PluginConfig config;
    private @Inject ActivityPlayerService activityPlayerService;
    private @Inject ActivityPlayerRepository repository;
    private @Inject PlatformScheduler scheduler;

    @EventHandler(priority = EventPriority.MONITOR)
    void handle(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission(config.staffPermission)) {
            return;
        }

        scheduler.runAsync(() -> {
            ActivityPlayer user = repository.getUser(player);
            activityPlayerService.getUncompletedActivityEntry(user).ifPresent(activityEntry -> {
                String message = event.getMessage();
                activityEntry.getActions().put(Instant.now(), new Action(ActionType.MESSAGE, message));
                user.save();
            });
        });
    }
}
