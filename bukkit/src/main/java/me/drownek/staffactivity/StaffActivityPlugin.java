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

    @Planned(ExecutionPhase.PRE_STARTUP)
    void preStartup(
        PluginConfig config
    ) {
        if (config.proxyMode && !config.storageConfig.storageType.equals(StorageType.MYSQL)) {
            getLogger().warning("Proxy mode is enabled, but storage type is not set to MYSQL, plugin won't update any data!");
        }
    }

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
