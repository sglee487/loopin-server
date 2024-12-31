package sg.snserver.hex.adapter_inbound.dto

import sg.snserver.hex.domain.entities.PlayItem
import sg.snserver.hex.domain.entities.Resource
import java.net.URL
import java.time.Instant

data class GetNewPlayItemResponseDTO(
    val playItemId: String,
    val publishedAt: Instant,
    val channelId: String,
    var title: String,
    var description: String?,
    var thumbnail: URL?,
    var channelTitle: String,
    var position: Long,
    val resource: ResourceResponseDTO,
    val videoOwnerChannelId: String?,
    var videoOwnerChannelTitle: String?,
    var startSeconds: Float = 0.0f,
    val platformType: PlatformTypeResponseDTO,
)

fun PlayItem.toResponseDTO(): GetNewPlayItemResponseDTO = GetNewPlayItemResponseDTO(
    playItemId = this.playItemId,
    publishedAt = publishedAt,
    channelId = channelId,
    title = title,
    description = description,
    thumbnail = thumbnail,
    channelTitle = channelTitle,
    position = position,
    resource = resource.toResponseDTO(),
    videoOwnerChannelId = videoOwnerChannelId,
    videoOwnerChannelTitle = videoOwnerChannelTitle,
    startSeconds = startSeconds,
    platformType = PlatformTypeResponseDTO.fromValue(platformType.value),
)

data class ResourceResponseDTO(
    val kind: String,
    val videoId: String
)

fun Resource.toResponseDTO() = ResourceResponseDTO(
    kind = kind,
    videoId = videoId,
)

data class LocalizedResponseDTO(
    val title: String,
    val description: String,
)
