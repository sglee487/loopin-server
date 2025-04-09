package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.*
import sg.snserver.hex.adapter_outbound.jpa.enums.PlatformTypeEntity
import sg.snserver.hex.domain.entities.Playlist
import java.net.URL
import java.time.Instant

@Entity
@Table(name = "sn_playlist")
data class PlaylistEntity(
    @Id
    val playlistId: String,
    val channelId: String,
    var title: String,

    var description: String?,
    var thumbnail: URL?,
    var channelTitle: String,

    @OneToOne(fetch = FetchType.LAZY)
    var localized: LocalizedEntity,

    @OneToOne(fetch = FetchType.LAZY)
    var contentDetails: ContentDetailsEntity,

    val publishedAt: Instant,

    @OneToMany(mappedBy = "playlist", cascade = [CascadeType.ALL], orphanRemoval = true)
    var items: MutableList<PlaylistItemEntity> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    val platformType: PlatformTypeEntity,
) : BaseEntity() {
    fun toDomain(
        itemsNull: Boolean = false,
    ): Playlist {
        return Playlist(
            playlistId = playlistId,
            channelId = channelId,
            title = title,
            description = description,
            thumbnail = thumbnail,
            channelTitle = channelTitle,
            localized = localized.toDomain(),
            contentDetails = contentDetails.toDomain(),
            publishedAt = publishedAt,
            platformType = platformType.toDomain(),
            items = if (itemsNull) null else items.map { it.playItem.toDomain() }.toMutableList(),
        )
    }
}