package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.*
import sg.snserver.hex.adapter_outbound.jpa.enums.PlatformTypeEntity
import sg.snserver.hex.domain.entities.PlayItem
import java.net.URL
import java.time.Instant

@Entity
@Table(name = "sn_play_item")
data class PlayItemEntity(
    @Id
    val playItemId: String,

    val publishedAt: Instant,
    val channelId: String,
    var title: String,

    @Column(columnDefinition = "TEXT")
    var description: String?,
    var thumbnail: URL?,
    var channelTitle: String,

    var position: Long,

    @ManyToOne(fetch = FetchType.EAGER)
    val resource: ResourceEntity,

    val videoOwnerChannelId: String?,
    var videoOwnerChannelTitle: String?,
    var startSeconds: Float = 0.0f,
    var isDeleted: Boolean = false,

    @Enumerated(EnumType.STRING)
    val platformType: PlatformTypeEntity,
) : BaseEntity() {
    fun toDomain(): PlayItem = PlayItem(
        playItemId = playItemId,
        publishedAt = publishedAt,
        channelId = channelId,
        title = title,
        description = description,
        thumbnail = thumbnail,
        channelTitle = channelTitle,
        position = position,
        resource = resource.toDomain(),
        videoOwnerChannelId = videoOwnerChannelId,
        videoOwnerChannelTitle = videoOwnerChannelTitle,
        startSeconds = startSeconds,
        isDeleted = isDeleted,
        platformType = platformType.toDomain(),
    )
}