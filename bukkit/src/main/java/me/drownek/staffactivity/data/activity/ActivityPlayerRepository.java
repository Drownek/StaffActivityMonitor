package me.drownek.staffactivity.data.activity;

import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.repository.DocumentRepository;
import eu.okaeri.persistence.repository.annotation.DocumentCollection;
import me.drownek.platform.core.annotation.DependsOn;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

@DependsOn(
    type = DocumentPersistence.class,
    name = "persistence"
)
@DocumentCollection(path = "users", keyLength = 36)
public interface ActivityPlayerRepository extends DocumentRepository<UUID, ActivityPlayer> {

    default ActivityPlayer getUser(OfflinePlayer offlinePlayer) {
        return findOrCreateByPath(offlinePlayer.getUniqueId());
    }
}
