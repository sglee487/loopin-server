package sg.snserver.hex.adapter_outbound.jpa.entities

import sg.snserver.hex.domain.entities.Resource

data class ResourceEntity(
    val kind: String,
    val videoId: String,
) {
    fun toDomain(): Resource = Resource(
        kind = kind,
        videoId = videoId,
    )
}
