package me.drownek.staffactivity.listener;

import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.PluginConfig;
import me.drownek.staffactivity.StaffActivityPlugin;
import me.drownek.staffactivity.core.ActivityEntry;
import me.drownek.staffactivity.core.ActivityPlayer;
import me.drownek.staffactivity.core.action.Action;
import me.drownek.staffactivity.core.action.ActionType;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.time.Instant;

@Component
public class PlayerChatListener implements Listener {

    private @Inject PluginConfig config;
    private @Inject ActivityPlayerService activityPlayerService;
    private @Inject ActivityPlayerRepository repository;
    private @Inject StaffActivityPlugin plugin;

    @EventHandler
    public void handle(ChatEvent event) {
        Connection sender = event.getSender();

        if (!(sender instanceof ProxiedPlayer player)) {
            return;
        }

        if (!player.hasPermission(config.staffPermission)) {
            return;
        }

        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            ActivityPlayer user = repository.getUser(player);

            // Create new entry in case it hadn't been created yet
            ActivityEntry activityEntry = activityPlayerService.getUncompletedActivityEntry(user)
                .orElseGet(() -> {
                    ActivityEntry newEntry = new ActivityEntry(Instant.now());
                    user.getEntries().add(newEntry);
                    return newEntry;
                });

            String message = event.getMessage();
            activityEntry.getActions().put(Instant.now().toEpochMilli(), new Action(Instant.now(), ActionType.MESSAGE, message));
            user.save();
        });
    }
}
