package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import sg.snserver.hex.domain.entities.Resource

@Entity
@Table(name = "sn_resource")
data class ResourceEntity(
    @Id
    val videoId: String,

    val kind: String,
) {
    fun toDomain(): Resource = Resource(
        videoId = videoId,
        kind = kind,
    )
}
