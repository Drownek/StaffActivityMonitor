package me.drownek.staffactivity.command;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.injector.OkaeriInjector;
import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.bukkit.LightBukkitPlugin;
import me.drownek.staffactivity.StaffActivityPlugin;
import me.drownek.staffactivity.config.Messages;
import org.bukkit.entity.Player;

import java.util.logging.Level;

@Command(name = "staffactivity reload")
@Permission("staffactivity.commands")
public class ReloadCommand {

    private @Inject Messages messages;
    private @Inject OkaeriInjector injector;
    private @Inject StaffActivityPlugin plugin;
    private @Inject LightBukkitPlugin lightBukkitPlugin;

    @Execute
    void handle(@Context Player player) {
        try {
            injector.streamOf(OkaeriConfig.class).forEach(OkaeriConfig::load);
            //noinspection unchecked
            injector.get("commands", LiteCommands.class).ifPresent(
                commands -> messages.liteCommandsConfig.apply(commands)
            );
            messages.configReloaded.sendTo(player);
        } catch (Exception e) {
            messages.configReloadFail.sendTo(player);
            plugin.getLogger().log(Level.SEVERE, "Failed to reload config", e);
        }
    }
}
