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

    default ActivityPlayer getUser(Player player) {
        return findOrCreateByPath(player.getUniqueId());
    }
}
