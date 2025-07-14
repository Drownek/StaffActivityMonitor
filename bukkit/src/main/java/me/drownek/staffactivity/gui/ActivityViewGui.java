package me.drownek.staffactivity.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.bukkit.scheduler.PlatformScheduler;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.config.PluginConfig;
import me.drownek.staffactivity.core.ActivityEntry;
import me.drownek.staffactivity.core.ActivityPlayer;
import me.drownek.staffactivity.core.action.Action;
import me.drownek.staffactivity.core.action.ActionType;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import me.drownek.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import panda.std.collection.ReversedIterable;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class ActivityViewGui {

    private @Inject ActivityPlayerRepository repository;
    private @Inject PluginConfig config;
    private @Inject PlatformScheduler scheduler;

    public void openActivityListGui(HumanEntity player) {
        scheduler.runAsync(() -> {
            Collection<ActivityPlayer> activities = repository.findAll();

            PaginatedGui gui = config.viewGuiList.toPaginatedGuiBuilder().disableAllInteractions().create();

            for (ActivityPlayer activityPlayer : activities) {
                UUID playerUuid = activityPlayer.getUuid();
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUuid);
                String name = Optional.ofNullable(offlinePlayer.getName()).orElse(playerUuid.toString());

                ItemStack viewGuiListItem = config.viewGuiListItem
                    .with("{player}", name)
                    .getItemStack();

                gui.addItem(ItemBuilder.skull(viewGuiListItem).owner(offlinePlayer).asGuiItem(event ->
                    openActivityViewForPlayer(player, activityPlayer, this::openActivityListGui)
                ));
            }
            scheduler.runSync(() -> gui.open(player));
        });
    }

    public void openActivityViewForPlayer(HumanEntity player, ActivityPlayer activityPlayer, Consumer<HumanEntity> closeAction) {
        openActivityViewForPlayer(player, activityPlayer, closeAction, true);
    }

    public void openActivityViewForPlayer(HumanEntity player, ActivityPlayer activityPlayer, Consumer<HumanEntity> closeAction, boolean oldestToNewest) {
        System.out.println("ActivityViewGui.openActivityViewForPlayer");
        System.out.println(activityPlayer.getUuid());

        PaginatedGui gui = config.viewGuiPlayer
            .closeAction(closeAction)
            .toPaginatedGuiBuilder()
            .disableAllInteractions()
            .create();

        var entries = oldestToNewest ? activityPlayer.getEntries() : new ReversedIterable<>(activityPlayer.getEntries());
        for (ActivityEntry activityEntry : entries) {
            var startTime = TimeUtil.formatTimeMillisToDate(activityEntry.getStartTime().toEpochMilli());
            Instant endTimestamp = activityEntry.getEndTime();
            var endTime = endTimestamp == null ? "now" : TimeUtil.formatTimeMillisToDate(endTimestamp.toEpochMilli());

            Map<Long, Action> actions = activityEntry.getActions();
            System.out.println("actions.size() = " + actions.size());
            long commandCount = actions.values().stream()
                .filter(action -> action.getType().equals(ActionType.COMMAND))
                .count();
            long messageCount = actions.values().stream()
                .filter(action -> action.getType().equals(ActionType.MESSAGE))
                .count();

            GuiItem guiItem = config.viewGuiPlayerListItem
                .with("{start}", startTime)
                .with("{end}", endTime)
                .with("{executed_commands}", commandCount)
                .with("{sent_messages}", messageCount)
                .asGuiItem(event ->
                    openViewMoreGui(player, activityPlayer, closeAction, oldestToNewest, actions)
                );

            gui.addItem(guiItem);
        }

        config.viewGuiPlayerSortItem
            .with("{oldest_to_newest_selection}", oldestToNewest ? config.viewGuiPlayerSortSelected : config.viewGuiPlayerSortUnselected)
            .with("{newest_to_oldest_selection}", oldestToNewest ? config.viewGuiPlayerSortUnselected : config.viewGuiPlayerSortSelected)
            .setGuiItem(gui, event ->
                openActivityViewForPlayer(player, activityPlayer, closeAction, !oldestToNewest)
            );
        gui.open(player);
    }

    private void openViewMoreGui(HumanEntity player, ActivityPlayer activityPlayer, Consumer<HumanEntity> closeAction, boolean oldestToNewest, Map<Long, Action> actions) {
        PaginatedGui viewMoreGui = config.viewGuiPlayerMore
            .closeAction(humanEntity ->
                openActivityViewForPlayer(humanEntity, activityPlayer, closeAction, oldestToNewest)
            )
            .toPaginatedGuiBuilder()
            .disableAllInteractions()
            .create();

        for (Action action : actions.values()) {
            boolean isCommand = action.getType().equals(ActionType.COMMAND);
            viewMoreGui.addItem(
                (isCommand ? config.viewGuiPlayerMoreCommandSent : config.viewGuiPlayerMoreMessageSent)
                    .with("{ACTION}", action.getMessage())
                    .with("{TIME}", TimeUtil.formatTimeMillisToDate(action.getTime().toEpochMilli()))
                    .asGuiItem()
            );
        }

        viewMoreGui.open(player);
    }
}
