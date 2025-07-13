package me.drownek.staffactivity;

import dev.rollczi.litecommands.LiteCommands;
import me.drownek.platform.bukkit.LightBukkitPlugin;
import me.drownek.platform.core.annotation.Scan;
import me.drownek.platform.core.plan.ExecutionPhase;
import me.drownek.platform.core.plan.Planned;
import me.drownek.staffactivity.config.Messages;
import me.drownek.staffactivity.config.PluginConfig;
import me.drownek.staffactivity.config.StorageType;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;
import org.bukkit.command.CommandSender;

@Scan(deep = true, exclusions = "me.drownek.staffactivity.libs")
public class StaffActivityPlugin extends LightBukkitPlugin {

    /**
     * Performs pre-startup checks for proxy mode and storage configuration.
     *
     * Logs a warning if proxy mode is enabled but the storage type is not set to MYSQL, indicating that the plugin will not update any data.
     */
    @Planned(ExecutionPhase.PRE_STARTUP)
    void preStartup(
        PluginConfig config
    ) {
        if (config.proxyMode && !config.storageConfig.storageType.equals(StorageType.MYSQL)) {
            getLogger().warning("Proxy mode is enabled, but storage type is not set to MYSQL, plugin won't update any data!");
        }
    }

    /**
     * Applies message-based command configurations and logs successful plugin startup.
     *
     * @param messages  the Messages instance containing command configurations
     * @param commands  the LiteCommands instance to configure
     */
    @Planned(ExecutionPhase.POST_STARTUP)
    void postStartup(
        Messages messages,
        LiteCommands<CommandSender> commands
    ) {
        messages.liteCommandsConfig.apply(commands);
        log("Plugin loaded successfully!");
    }

    @Planned(ExecutionPhase.SHUTDOWN)
    void shutdown(
        ActivityPlayerService activityPlayerService
    ) {
        activityPlayerService.completeAllActiveEntries();
        log("Plugin unloaded successfully!");
    }
}
