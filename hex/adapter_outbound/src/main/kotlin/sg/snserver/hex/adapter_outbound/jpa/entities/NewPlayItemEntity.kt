package sg.snserver.hex.adapter_outbound.jpa.entities

import org.springframework.data.mongodb.core.mapping.Document
import java.net.URL
import java.time.Instant

@Document
data class NewPlayItemEntity(
    val publishedAt: Instant,
    val channelId: String,
    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    val playListId: String,
    var position: Long,
    val resource: ResourceEntity,
    val videoOwnerChannelId: String?,
    var videoOwnerChannelTitle: String?,
    var startSeconds: Float = 0.0f,
) : BaseEntity() {
}