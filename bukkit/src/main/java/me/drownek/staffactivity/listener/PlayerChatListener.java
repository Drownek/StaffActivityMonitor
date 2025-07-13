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
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.time.Instant;

@Component
public class PlayerChatListener implements Listener {

    private @Inject PluginConfig config;
    private @Inject ActivityPlayerService activityPlayerService;
    private @Inject ActivityPlayerRepository repository;
    private @Inject PlatformScheduler scheduler;

    /**
     * Handles asynchronous player chat events to record staff chat messages as activity actions.
     *
     * If the plugin is not in proxy mode and the player has the required staff permission, schedules an asynchronous task to log the chat message as an action for the player's current uncompleted activity entry.
     *
     * @param event the asynchronous player chat event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    void handle(AsyncPlayerChatEvent event) {
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
                String message = event.getMessage();
                activityEntry.getActions().put(Instant.now().toEpochMilli(), new Action(ActionType.MESSAGE, message));
                user.save();
            });
        });
    }
}
