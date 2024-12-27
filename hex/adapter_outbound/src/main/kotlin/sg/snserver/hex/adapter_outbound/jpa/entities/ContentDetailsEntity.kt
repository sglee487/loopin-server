package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import sg.snserver.hex.domain.entities.ContentDetails

@Entity
@Table(name = "sn_content_details")
data class ContentDetailsEntity(
    @Id
    val playlistId: String,

    var itemCount: Long,
) {
    fun toDomain(): ContentDetails = ContentDetails(
        playlistId = playlistId,
        itemCount = itemCount,
    )
}