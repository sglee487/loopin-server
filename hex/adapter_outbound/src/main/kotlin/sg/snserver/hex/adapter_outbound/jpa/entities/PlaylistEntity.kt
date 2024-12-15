package sg.snserver.hex.adapter_outbound.jpa.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.net.URL
import java.time.Instant

@Document
data class PlaylistEntity(
    @Id val playlistId: String, // @Id must be initialized to decide duplicate or not (here, with playListId)

    val channelId: String,
    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    var localized: LocalizedEntity,
    var contentDetails: ContentDetailsEntity,
    val publishedAt: Instant,
    var items: MutableList<NewPlayItemEntity> = mutableListOf(),
): BaseEntity() {
}