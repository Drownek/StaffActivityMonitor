package me.drownek.staffactivity.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.drownek.platform.bukkit.commands.LiteCommandsConfig;
import me.drownek.platform.core.annotation.Configuration;
import me.drownek.util.SendableMessage;

@SuppressWarnings("CanBeFinal")
@Configuration(path = "messages.{ext}")
public class Messages extends OkaeriConfig {
    public SendableMessage configReloaded = SendableMessage.of("<#67db6c>Config reloaded!");
    public SendableMessage configReloadFail = SendableMessage.of("<#FF415C>Config failed to load, check console errors!");
    public String playerNotFound = "Player not found.";

    @Comment("Messages for commands library, note that not all of them are used in the plugin!")
    public LiteCommandsConfig liteCommandsConfig = new LiteCommandsConfig();
}
