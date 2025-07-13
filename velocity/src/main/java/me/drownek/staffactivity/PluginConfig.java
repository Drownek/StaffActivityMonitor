package me.drownek.staffactivity;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.drownek.platform.core.annotation.Configuration;

@Configuration
public class PluginConfig extends OkaeriConfig {

    public String staffPermission = "staffactivity.staff";

    @Comment("MYSQL is required for proxy, and make sure to enable proxyMode in all bukkit instances!")
    public StorageConfig storageConfig = new StorageConfig();

    public static class StorageConfig extends OkaeriConfig {
        public String prefix = "StaffActivityMonitor";
        public String uri = "jdbc:mysql://localhost:3306/mydatabase?user=user123&password=secretpass";
    }
}
