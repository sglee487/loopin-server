package sg.snserver.hex.adapter_outbound.jpa.interfaces

import org.springframework.data.mongodb.repository.MongoRepository
import sg.snserver.hex.adapter_outbound.jpa.entities.PlaylistEntity
import java.util.UUID

interface PlaylistRepositoryJpa: MongoRepository<PlaylistEntity, UUID> {
}