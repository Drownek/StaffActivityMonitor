package me.drownek.staffactivity.data;

import com.zaxxer.hikari.HikariConfig;
import eu.okaeri.configs.json.simple.JsonSimpleConfigurer;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.persistence.PersistencePath;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.jdbc.MariaDbPersistence;
import me.drownek.platform.bukkit.persistence.YamlBukkitPersistence;
import me.drownek.platform.core.annotation.Bean;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.config.PluginConfig;

import java.io.File;

@Component
public class PersistenceConfigurer {

    /**
     * Creates and configures a {@link DocumentPersistence} instance based on the storage type specified in the provided configuration.
     *
     * Depending on the storage type, this method returns either a MySQL-backed persistence layer using MariaDB and HikariCP, or a flat file YAML-based persistence layer using the given data folder.
     *
     * @param dataFolder the folder where flat file data will be stored if flat file persistence is selected
     * @param config the plugin configuration containing storage settings
     * @return a configured {@link DocumentPersistence} instance for the selected storage type
     */
    @Bean("persistence")
    public DocumentPersistence configurePersistence(@Inject("dataFolder") File dataFolder, PluginConfig config) {
        return switch (config.storageConfig.storageType) {
            case MYSQL -> {
                // @formatter:off
                try { Class.forName("org.mariadb.jdbc.Driver"); } catch (ClassNotFoundException ignored) { }
                // @formatter:on

                PersistencePath basePath = PersistencePath.of(config.storageConfig.prefix);

                HikariConfig mariadbHikari = new HikariConfig();
                mariadbHikari.setJdbcUrl(config.storageConfig.uri);
                yield new DocumentPersistence(new MariaDbPersistence(basePath, mariadbHikari), JsonSimpleConfigurer::new);
            }
            case FLAT -> YamlBukkitPersistence.of(dataFolder);
        };
    }
}
