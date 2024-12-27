package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import sg.snserver.hex.domain.entities.Localized

@Entity
@Table(name = "sn_localized")
data class LocalizedEntity(
    @Id
    val videoId: String,

    val title: String,
    val description: String,
) {
    fun toDomain(): Localized = Localized(
        playlistId = videoId,
        title = title,
        description = description,
    )
}