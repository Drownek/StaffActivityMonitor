package me.drownek.staffactivity.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import me.drownek.staffactivity.data.activity.ActivityPlayer;
import me.drownek.staffactivity.gui.ActivityViewGui;
import org.bukkit.entity.Player;

@Command(name = "staffactivity view")
@Permission("staffactivity.commands")
public class ActivityViewCommand {

    private @Inject BukkitTasker tasker;
    private @Inject ActivityViewGui activityViewGui;

    @Execute
    void view(@Context Player player) {
        activityViewGui.openActivityListGui(player);
    }

    @Execute
    void view(@Context Player player, @Arg ActivityPlayer activityPlayer) {
        activityViewGui.openActivityViewForPlayer(player, activityPlayer, null);
    }
}
