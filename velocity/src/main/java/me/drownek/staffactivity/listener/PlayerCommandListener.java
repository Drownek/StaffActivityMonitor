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
                String command = event.getCommand().startsWith("/") ? event.getCommand() : "/" + event.getCommand();
                activityEntry.getActions().put(Instant.now().toEpochMilli(), new Action(ActionType.COMMAND, command));
                user.save();
            });
        });
    }
}
