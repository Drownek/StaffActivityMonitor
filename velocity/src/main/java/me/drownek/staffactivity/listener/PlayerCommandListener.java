package me.drownek.staffactivity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
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
public class PlayerCommandListener implements Listener {

    private @Inject PluginConfig config;
    private @Inject ActivityPlayerRepository repository;
    private @Inject PlatformScheduler scheduler;
    private @Inject ActivityPlayerService activityPlayerService;

    /**
     * Handles command execution events by staff players and records the command as an action in their current activity entry.
     *
     * Processes only commands executed by players with the required staff permission. If the player has an uncompleted activity entry, the executed command is logged as a `COMMAND` action with a timestamp. The update is performed asynchronously.
     *
     * @param event the command execution event to handle
     */
    @Subscribe
    void handle(CommandExecuteEvent event) {
        if (!(event.getCommandSource() instanceof Player player)) {
            return;
        }

        if (!player.hasPermission(config.staffPermission)) {
            return;
        }

        scheduler.runAsync(() -> {
            ActivityPlayer user = repository.getUser(player);
            activityPlayerService.getUncompletedActivityEntry(user).ifPresent(activityEntry -> {
                activityEntry.getActions().put(Instant.now().toEpochMilli(), new Action(ActionType.COMMAND, event.getCommand()));
                user.save();
            });
        });
    }
}
