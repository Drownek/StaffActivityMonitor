package me.drownek.staffactivity.data;

import com.zaxxer.hikari.HikariConfig;
import eu.okaeri.configs.json.simple.JsonSimpleConfigurer;
import eu.okaeri.persistence.PersistencePath;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.jdbc.MariaDbPersistence;
import me.drownek.platform.core.annotation.Bean;
import me.drownek.platform.core.annotation.Component;
import me.drownek.staffactivity.PluginConfig;

@Component
public class PersistenceConfigurer {

    /**
     * Configures and provides a DocumentPersistence instance backed by MariaDB using settings from the given plugin configuration.
     *
     * The returned persistence uses a JSON simple configurer for document serialization and is initialized with a MariaDB connection pool and a persistence path derived from the configuration.
     *
     * @param config the plugin configuration containing storage settings
     * @return a DocumentPersistence instance configured for MariaDB with JSON document support
     */
    @Bean("persistence")
    public DocumentPersistence configurePersistence(PluginConfig config) {
        // @formatter:off
        try { Class.forName("org.mariadb.jdbc.Driver"); } catch (ClassNotFoundException ignored) { }
        // @formatter:on

        PersistencePath basePath = PersistencePath.of(config.storageConfig.prefix);

        HikariConfig mariadbHikari = new HikariConfig();
        mariadbHikari.setJdbcUrl(config.storageConfig.uri);
        return new DocumentPersistence(new MariaDbPersistence(basePath, mariadbHikari), JsonSimpleConfigurer::new);
    }
}
