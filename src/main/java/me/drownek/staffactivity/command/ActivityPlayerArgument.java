package me.drownek.staffactivity.command;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import eu.okaeri.injector.annotation.Inject;
import me.drownek.platform.bukkit.annotation.CommandArgument;
import me.drownek.staffactivity.config.Messages;
import me.drownek.staffactivity.data.activity.ActivityPlayer;
import me.drownek.staffactivity.data.activity.ActivityPlayerRepository;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Objects;

@CommandArgument
public class ActivityPlayerArgument extends ArgumentResolver<CommandSender, ActivityPlayer> {

    private @Inject ActivityPlayerRepository repository;
    private @Inject Messages messages;

    @Override
    protected ParseResult<ActivityPlayer> parse(Invocation<CommandSender> invocation, Argument<ActivityPlayer> argument, String s) {
        return repository.streamAll()
            .filter(activityPlayer -> {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(activityPlayer.getUuid());
                return offlinePlayer.getName() != null && offlinePlayer.getName().equals(s);
            })
            .findAny()
            .map(ParseResult::success)
            .orElse(ParseResult.failure(messages.playerNotFound));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<ActivityPlayer> argument, SuggestionContext context) {
        return repository.streamAll()
            .map(activityPlayer -> {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(activityPlayer.getUuid());
                return offlinePlayer.getName();
            })
            .filter(Objects::nonNull)
            .collect(SuggestionResult.collector());
    }
}
