package sg.snserver.hex.adapter_outbound.jpa.entities

import org.springframework.data.mongodb.core.mapping.Document
import sg.snserver.hex.domain.entities.Plays
import java.util.*

@Document
data class UserPlaysEntity(
    val id: UUID,

    val plays: Plays,
): BaseEntity()
