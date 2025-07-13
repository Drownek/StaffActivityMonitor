package me.drownek.staffactivity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.velocity.component.type.listener.Listener;
import eu.okaeri.platform.velocity.scheduler.PlatformScheduler;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.PluginConfig;
import me.drownek.staffactivity.core.ActivityPlayer;
import me.drownek.staffactivity.core.action.Action;
import me.drownek.staffactivity.core.action.ActionType;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;

import java.time.Instant;

@Component
public class PlayerChatListener implements Listener {

    private @Inject PluginConfig config;
    private @Inject ActivityPlayerService activityPlayerService;
    private @Inject ActivityPlayerRepository repository;
    private @Inject PlatformScheduler scheduler;

    /**
     * Handles player chat events by recording staff chat messages as actions for activity tracking.
     *
     * If the player has the required staff permission, schedules an asynchronous task to log the chat message
     * as a `MESSAGE` action in the player's current uncompleted activity entry.
     *
     * @param event the player chat event containing the player and message information
     */
    @Subscribe
    void handle(PlayerChatEvent event) {
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
