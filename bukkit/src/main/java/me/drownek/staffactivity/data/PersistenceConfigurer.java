package me.drownek.staffactivity.data;

import com.zaxxer.hikari.HikariConfig;
import eu.okaeri.configs.json.simple.JsonSimpleConfigurer;
import eu.okaeri.persistence.PersistencePath;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.jdbc.MariaDbPersistence;
import me.drownek.platform.bukkit.persistence.YamlBukkitPersistence;
import me.drownek.platform.core.annotation.Bean;
import me.drownek.platform.core.annotation.Component;
import me.drownek.platform.core.persistence.PostgresPersistence;
import me.drownek.staffactivity.config.PluginConfig;
import org.bukkit.plugin.Plugin;

@Component
public class PersistenceConfigurer {

    @Bean(value = "persistence")
    public DocumentPersistence configurePersistence(Plugin plugin, PluginConfig config) {

        // remember that if plugin is not intended to have shared state
        // between multiple instances you must allow users to set persistence's
        // basePath manually or add some other possibility to differ keys
        PersistencePath basePath = PersistencePath.of(config.storage.prefix);

        // multiple backends are possible with an easy switch
        switch (config.storage.backend) {
            case FLAT:
                return YamlBukkitPersistence.of(plugin);
            case MYSQL:
                // setup hikari based on your needs, e.g. using config
                HikariConfig mariadbHikari = new HikariConfig();
                mariadbHikari.setJdbcUrl(config.storage.uri);
                mariadbHikari.setUsername(config.storage.user);
                mariadbHikari.setPassword(config.storage.password);
                // it is REQUIRED to use json configurer for the mariadb backend
                return new DocumentPersistence(new MariaDbPersistence(basePath, mariadbHikari), JsonSimpleConfigurer::new);
            case POSTGRES:
                // setup hikari based on your needs, e.g. using config
                HikariConfig postgresHikari = new HikariConfig();
                postgresHikari.setJdbcUrl(config.storage.uri);
                postgresHikari.setUsername(config.storage.user);
                postgresHikari.setPassword(config.storage.password);
                postgresHikari.setDriverClassName("org.postgresql.Driver");
                // it is REQUIRED to use json configurer for the mariadb backend
                return new DocumentPersistence(new PostgresPersistence(basePath, postgresHikari), JsonSimpleConfigurer::new);
            default:
                throw new IllegalStateException("Unexpected value: " + config.storage.backend);
        }
    }
}
