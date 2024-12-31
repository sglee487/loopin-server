package sg.snserver.hex.adapter_inbound.dto

import org.springframework.data.domain.Page
import sg.snserver.hex.domain.entities.CurrentPlay
import sg.snserver.hex.domain.entities.PlayItem
import sg.snserver.hex.domain.entities.Resource
import sg.snserver.hex.domain.enums.PlatformType
import java.net.URL
import java.time.Instant

data class SaveCurrentPlayRequestDTO(
    val playlistId: String,
    val nowPlayingItem: SaveNowPlayingItemDTO,
    val prevItemIdList: List<String>,
    val nextItemIdList: List<String>,
)

data class SaveNowPlayingItemDTO(
    val playItemId: String,

    val publishedAt: Instant,
    val channelId: String,
    var title: String,
    var description: String?,
    var thumbnail: URL?,
    var channelTitle: String,
    var position: Long,
    val resource: ResourceRequestDTO,
    val videoOwnerChannelId: String?,
    var videoOwnerChannelTitle: String?,
    var startSeconds: Float = 0.0f,
    var isDeleted: Boolean = false,
    val platformType: PlatformTypeRequestDTO,
) {
    fun toDomain() = PlayItem(
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

data class GetCurrentPlayRequestDTO(
    val nowPlayingItem: GetNewPlayItemResponseDTO,
    val playlist: GetPlaylistResponseDTO,
    val prev: List<GetNewPlayItemResponseDTO>?,
    val next: List<GetNewPlayItemResponseDTO>?,
    val prevLength: Int,
    val nextLength: Int,
)

fun CurrentPlay.toResponseDTO() = GetCurrentPlayRequestDTO(
    nowPlayingItem = nowPlayingItem.toResponseDTO(),
    playlist = playlist.toResponseDTO(),
    prev = prev.map { it.toResponseDTO() },
    next = next.map { it.toResponseDTO() },
    prevLength = prev.size,
    nextLength = next.size
)

data class GetCurrentPlayBatchResponseDTO(
    val currentPlays: Page<GetCurrentPlayRequestDTO>,
)

data class ResourceRequestDTO(
    val kind: String,
    val videoId: String,
) {
    fun toDomain() = Resource(
        videoId = videoId,
        kind = kind,
    )
}

data class SaveCurrentPlayItemDTO(
    val playItemId: String,
)

enum class PlatformTypeRequestDTO(
    val value: String,
) {
    YOUTUBE("youtube");

    companion object {
        fun fromValue(value: String): PlatformTypeRequestDTO {
            return PlatformTypeRequestDTO.entries.find { it.value.equals(value, true) }!!
        }
    }

    fun toDomain() = PlatformType.fromValue(value)
}

enum class PlatformTypeResponseDTO(
    val value: String,
) {
    YOUTUBE("youtube");

    companion object {
        fun fromValue(value: String): PlatformTypeResponseDTO {
            return PlatformTypeResponseDTO.entries.find { it.value.equals(value, true) }!!
        }
    }
}