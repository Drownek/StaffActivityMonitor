package me.drownek.staffactivity.data.activity;

import com.velocitypowered.api.proxy.Player;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.repository.DocumentRepository;
import eu.okaeri.persistence.repository.annotation.DocumentCollection;
import me.drownek.platform.core.annotation.DependsOn;
import me.drownek.staffactivity.core.ActivityPlayer;

import java.util.UUID;

@DependsOn(
    type = DocumentPersistence.class,
    name = "persistence"
)
@DocumentCollection(path = "users", keyLength = 36)
public interface ActivityPlayerRepository extends DocumentRepository<UUID, ActivityPlayer> {

    /**
     * Retrieves the {@link ActivityPlayer} associated with the given player, creating a new record if one does not exist.
     *
     * @param player the player whose activity data is to be retrieved
     * @return the {@link ActivityPlayer} corresponding to the player's UUID
     */
    default ActivityPlayer getUser(Player player) {
        return findOrCreateByPath(player.getUniqueId());
    }
}
