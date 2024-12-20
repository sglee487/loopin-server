package sg.snserver.hex.adapter_outbound.jpa.entities

import sg.snserver.hex.domain.entities.ContentDetails

data class ContentDetailsEntity(
    var itemCount: Long,
) {
    fun toDomain(): ContentDetails = ContentDetails(
        itemCount = itemCount,
    )
}