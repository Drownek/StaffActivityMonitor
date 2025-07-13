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
