package sg.snserver.hex.adapter_inbound.dto

import sg.snserver.hex.domain.entities.ContentDetails
import sg.snserver.hex.domain.entities.Localized
import sg.snserver.hex.domain.entities.NewPlayItem
import sg.snserver.hex.domain.entities.Resource
import java.net.URL
import java.time.Instant

data class GetNewPlayItemResponseDTO(
    val publishedAt: Instant,
    val channelId: String,
    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    val playListId: String,
    var position: Long,
    val resource: ResourceResponseDTO,
    val videoOwnerChannelId: String?,
    var videoOwnerChannelTitle: String?,
    var startSeconds: Float = 0.0f,
)

fun NewPlayItem.toResponseDTO(): GetNewPlayItemResponseDTO = GetNewPlayItemResponseDTO(
    publishedAt = publishedAt,
    channelId = channelId,
    title = title,
    description = description,
    thumbnail = thumbnail,
    channelTitle = channelTitle,
    playListId = playListId,
    position = position,
    resource = resource.toResponseDTO(),
    videoOwnerChannelId = videoOwnerChannelId,
    videoOwnerChannelTitle = videoOwnerChannelTitle,
    startSeconds = startSeconds,
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
