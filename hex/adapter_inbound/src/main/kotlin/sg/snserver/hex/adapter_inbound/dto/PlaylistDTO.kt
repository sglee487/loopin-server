package sg.snserver.hex.adapter_inbound.dto

import sg.snserver.hex.domain.entities.ContentDetails
import sg.snserver.hex.domain.entities.Localized
import sg.snserver.hex.domain.entities.Playlist
import java.net.URL
import java.time.Instant

data class CreatePlaylistRequestDTO(
    val playlistId: String,
    val refresh: Boolean = false,
)

data class GetPlaylistResponseDTO(
    val playlistId: String,
    val channelId: String,
    var title: String,
    var description: String?,
    var thumbnail: URL?,
    var channelTitle: String,
    var localized: LocalizedResponseDTO,
    var contentDetails: ContentDetailsResponseDTO,
    var items: MutableList<GetNewPlayItemResponseDTO>? = mutableListOf(),
    val publishedAt: Instant,

    var updatedAt: Instant,
    val platformType: PlatformTypeResponseDTO,
)

data class GetPlaylistBatchResponseDTO(
    val playlistId: String,
    val channelId: String,
    var title: String,
    var description: String?,
    var thumbnail: URL?,
    var channelTitle: String,
    var localized: LocalizedResponseDTO,
    var contentDetails: ContentDetailsResponseDTO,
    var items: MutableList<GetNewPlayItemResponseDTO>? = mutableListOf(),
    val publishedAt: Instant,

    var updatedAt: Instant,

    val platformType: PlatformTypeResponseDTO
)


fun Playlist.toResponseDTO(): GetPlaylistResponseDTO = GetPlaylistResponseDTO(
    playlistId = playlistId,
    channelId = channelId,
    title = title,
    description = description,
    thumbnail = thumbnail,
    channelTitle = channelTitle,
    localized = localized.toResponseDTO(),
    contentDetails = contentDetails.toResponseDTO(),
    items = items?.map { it.toResponseDTO() }?.toMutableList(),
    publishedAt = publishedAt,
    updatedAt = updatedAt,
    platformType = PlatformTypeResponseDTO.fromValue(platformType.value)
)


fun Localized.toResponseDTO() = LocalizedResponseDTO(
    title = title,
    description = description,
)

data class ContentDetailsResponseDTO(
    val itemCount: Long,
)

fun ContentDetails.toResponseDTO() = ContentDetailsResponseDTO(
    itemCount = itemCount,
)