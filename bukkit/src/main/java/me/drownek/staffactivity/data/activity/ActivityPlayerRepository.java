package me.drownek.staffactivity.data.activity;

import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.repository.DocumentRepository;
import eu.okaeri.persistence.repository.annotation.DocumentCollection;
import me.drownek.platform.core.annotation.DependsOn;
import me.drownek.staffactivity.core.ActivityPlayer;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

@DependsOn(
    type = DocumentPersistence.class,
    name = "persistence"
)
@DocumentCollection(path = "users", keyLength = 36)
public interface ActivityPlayerRepository extends DocumentRepository<UUID, ActivityPlayer> {

    /**
     * Retrieves the {@code ActivityPlayer} associated with the given {@code OfflinePlayer}, creating a new record if one does not exist.
     *
     * @param player the offline player whose activity data is to be retrieved
     * @return the corresponding {@code ActivityPlayer} instance
     */
    default ActivityPlayer getUser(OfflinePlayer player) {
        return findOrCreateByPath(player.getUniqueId());
    }
}
