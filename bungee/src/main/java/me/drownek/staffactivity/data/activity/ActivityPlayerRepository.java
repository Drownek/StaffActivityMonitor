package me.drownek.staffactivity.data.activity;

import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.repository.DocumentRepository;
import eu.okaeri.persistence.repository.annotation.DocumentCollection;
import me.drownek.platform.core.annotation.DependsOn;
import me.drownek.staffactivity.core.ActivityPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@DependsOn(
    type = DocumentPersistence.class,
    name = "persistence"
)
@DocumentCollection(path = "users", keyLength = 36)
public interface ActivityPlayerRepository extends DocumentRepository<UUID, ActivityPlayer> {

    default ActivityPlayer getUser(ProxiedPlayer player) {
        return findOrCreateByPath(player.getUniqueId());
    }
}
