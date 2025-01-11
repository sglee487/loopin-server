package sg.snserver.hex.adapter_inbound.dto

import sg.snserver.hex.domain.entities.CurrentPlay
import sg.snserver.hex.domain.entities.PlayItem
import sg.snserver.hex.domain.entities.Resource
import sg.snserver.hex.domain.enums.PlatformType
import java.net.URL
import java.time.Instant

data class SaveCurrentPlayRequestDTO(
    val nowPlayingItem: SaveNowPlayingItemDTO?,
    val prevItemIdList: List<String>,
    val nextItemIdList: List<String>,
    val startSeconds: Float,
)

data class SaveCurrentPlayStartSecondsRequestDTO(
    val startSeconds: Float,
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
        isDeleted = isDeleted,
        platformType = platformType.toDomain(),
    )
}

fun CurrentPlay.toResponseDTO() = CurrentPlayResponseDTO(
    nowPlayingItem = nowPlayingItem?.toResponseDTO(),
    playlist = playlist.toResponseDTO(),
    prev = prev.map { it.toResponseDTO() },
    next = next.map { it.toResponseDTO() },
    startSeconds = startSeconds,
    prevLength = prev.size,
    nextLength = next.size,
)

fun CurrentPlay.toBatchSingleResponseDTO() = CurrentPlayResponseDTO(
    nowPlayingItem = nowPlayingItem?.toResponseDTO(),
    playlist = playlist.toResponseDTO(),
    prev = prev.map { it.toResponseDTO() },
    next = next.map { it.toResponseDTO() },
    startSeconds = startSeconds,
    prevLength = prev.size,
    nextLength = next.size
)

data class GetCurrentPlayBatchResponseDTO(
    val currentPlayMap: Map<String, CurrentPlayResponseDTO>
)

data class CurrentPlayResponseDTO(
    val nowPlayingItem: GetNewPlayItemResponseDTO?,
    val playlist: GetPlaylistResponseDTO,
    val prev: List<GetNewPlayItemResponseDTO>?,
    val next: List<GetNewPlayItemResponseDTO>?,
    val startSeconds: Float,
    val prevLength: Int,
    val nextLength: Int,
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