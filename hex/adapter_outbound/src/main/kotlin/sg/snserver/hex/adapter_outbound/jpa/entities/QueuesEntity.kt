package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("sn_queues")
data class QueuesEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val playlistId: String,
    var prev: List<String>,
    var next: List<String>,
)
