package sg.snserver.hex.adapter_outbound.jpa.interfaces

import org.springframework.data.mongodb.repository.MongoRepository
import sg.snserver.hex.adapter_outbound.jpa.entities.QueuesEntity
import java.util.*

interface QueuesRepositoryJpa: MongoRepository<QueuesEntity, String> {
    fun findByUserIdAndPlaylistId(userId: String, playlistId: String): QueuesEntity?
}