package me.drownek.staffactivity.listener;

import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.bukkit.scheduler.PlatformScheduler;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.config.PluginConfig;
import me.drownek.staffactivity.core.ActivityEntry;
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
import java.util.logging.Logger;

@Component
public class PlayerCommandListener implements Listener {

    private @Inject PluginConfig config;
    private @Inject ActivityPlayerRepository repository;
    private @Inject PlatformScheduler scheduler;
    private @Inject ActivityPlayerService activityPlayerService;
    private @Inject Logger logger;

    @EventHandler(priority = EventPriority.MONITOR)
    void handle(PlayerCommandPreprocessEvent event) {
        logger.info("[DEBUG] PlayerCommandListener.handle() called - Command: " + event.getMessage());

        if (config.proxyMode) {
            logger.info("[DEBUG] Skipping command tracking - proxy mode is enabled");
            return;
        }

        Player player = event.getPlayer();
        logger.info("[DEBUG] Player: " + player.getName() + " (UUID: " + player.getUniqueId() + ")");

        if (!player.hasPermission(config.staffPermission)) {
            logger.info("[DEBUG] Player " + player.getName() + " does not have staff permission: " + config.staffPermission);
            return;
        }

        logger.info("[DEBUG] Player has staff permission - scheduling async task");
            scheduler.runAsync(() -> {
                logger.info("[DEBUG] Async task started for player: " + player.getName());

                    ActivityPlayer user = repository.getUser(player);
                logger.info("[DEBUG] Retrieved ActivityPlayer for: " + player.getName() + " - Entries count: " + user.getEntries().size());

                    // Create new entry in case it hadn't been created yet
                    ActivityEntry activityEntry = activityPlayerService.getUncompletedActivityEntry(user)
                        .orElseGet(() -> {
                            logger.info("[DEBUG] Creating new ActivityEntry for player: " + player.getName());
                                ActivityEntry newEntry = new ActivityEntry(Instant.now());
                            user.getEntries().add(newEntry);
                            return newEntry;
                        });

                if (activityPlayerService.getUncompletedActivityEntry(user).isPresent()) {
                    logger.info("[DEBUG] Using existing uncompleted ActivityEntry for player: " + player.getName());
                }

                String command = event.getMessage().startsWith("/") ? event.getMessage() : "/" + event.getMessage();
                    logger.info("[DEBUG] Recording command: " + command);

                        activityEntry.getActions().put(Instant.now().toEpochMilli(), new Action(Instant.now(), ActionType.COMMAND, command));
                logger.info("[DEBUG] Action added - Total actions in entry: " + activityEntry.getActions().size());

                    user.save();
                logger.info("[DEBUG] User saved successfully for player: " + player.getName());
            });
    }
}
