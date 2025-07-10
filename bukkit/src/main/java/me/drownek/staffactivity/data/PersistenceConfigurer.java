package me.drownek.staffactivity.data;

import eu.okaeri.persistence.document.DocumentPersistence;
import me.drownek.platform.bukkit.persistence.YamlBukkitPersistence;
import me.drownek.platform.core.annotation.Bean;
import me.drownek.platform.core.annotation.Component;
import org.bukkit.plugin.Plugin;

@Component
public class PersistenceConfigurer {

    @Bean(value = "persistence")
    public DocumentPersistence configurePersistence(Plugin plugin) {
        return YamlBukkitPersistence.of(plugin);
    }
}
