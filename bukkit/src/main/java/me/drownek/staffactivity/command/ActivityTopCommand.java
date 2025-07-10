package me.drownek.staffactivity.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import me.drownek.staffactivity.gui.ActivityTopGui;
import org.bukkit.entity.Player;

@Command(name = "staffactivity")
@Permission("staffactivity.commands")
public class ActivityTopCommand {

    private @Inject BukkitTasker tasker;
    private @Inject ActivityTopGui activityTopGui;

    @Execute(name = "top")
    void top(@Context Player player) {
        activityTopGui.openActivityTopGui(player);
    }
}
