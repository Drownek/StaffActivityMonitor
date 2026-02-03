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

        @Comment("Database host")
        public String host = "localhost";

        @Comment("Database port (default 3306 for MySQL, 5432 for PostgreSQL)")
        public int port = 3306;

        @Comment("Database name")
        public String database = "db";

        public String user = "";

        public String password = "";
    }
}
