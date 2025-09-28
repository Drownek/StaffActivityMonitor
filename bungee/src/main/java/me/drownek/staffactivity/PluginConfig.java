package me.drownek.staffactivity;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.drownek.platform.core.annotation.Configuration;

@Configuration
public class PluginConfig extends OkaeriConfig {

    public String staffPermission = "staffactivity.staff";

    @Comment("Database configuration")
    @Comment("MYSQL or POSTGRES is required for proxy, and make sure to enable proxyMode in all bukkit instances!")
    public StorageConfig storage = new StorageConfig();

    public static class StorageConfig extends OkaeriConfig {

        @Comment("Type of the storage backend: MYSQL, POSTGRES")
        public StorageBackend backend = StorageBackend.MYSQL;

        @Comment("Prefix for the storage")
        public String prefix = "StaffActivityMonitor";

        @Comment("MYSQL  : jdbc:mysql://localhost:3306/db")
        @Comment("POSTGRES  : jdbc:postgresql://localhost:5432/db")
        public String uri = "jdbc:mysql://localhost:3306/db";

        public String user = "";

        public String password = "";
    }
}
