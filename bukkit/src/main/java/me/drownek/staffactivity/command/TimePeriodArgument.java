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
import me.drownek.staffactivity.core.report.TimePeriod;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

@CommandArgument
public class TimePeriodArgument extends ArgumentResolver<CommandSender, TimePeriod> {

    private @Inject Messages messages;

    @Override
    protected ParseResult<TimePeriod> parse(Invocation<CommandSender> invocation, Argument<TimePeriod> context, String argument) {
        try {
            return ParseResult.success(TimePeriod.valueOf(argument.toUpperCase().replace("-", "_")));
        } catch (IllegalArgumentException e) {
            return ParseResult.failure(messages.invalidTimePeriod.replace("{argument}", argument));
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<TimePeriod> argument, SuggestionContext context) {
        return SuggestionResult.of(
                Arrays.stream(TimePeriod.values())
                        .map(period -> period.name().toLowerCase().replace("_", "-"))
                        .toList()
        );
    }
}
