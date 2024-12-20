package sg.snserver.hex.adapter_outbound.jpa.mapper

import sg.snserver.hex.adapter_outbound.jpa.entities.LocalizedEntity
import sg.snserver.hex.adapter_outbound.jpa.entities.NewPlayItemEntity
import sg.snserver.hex.adapter_outbound.jpa.entities.PlaylistEntity
import sg.snserver.hex.adapter_outbound.jpa.entities.ResourceEntity
import sg.snserver.hex.adapter_outbound.jpa.entities.ContentDetailsEntity
import sg.snserver.hex.domain.entities.*

fun Playlist.toEntity() = PlaylistEntity(
    playlistId = playlistId,
    channelId = channelId,
    title = title,
    description = description,
    thumbnail = thumbnail,
    channelTitle = channelTitle,
    localized = localized.toEntity(),
    contentDetails = contentDetails.toEntity(),
    publishedAt = publishedAt,
    items = items?.map { it.toEntity() }?.toMutableList()!!,
)

fun NewPlayItem.toEntity() = NewPlayItemEntity(
    publishedAt = publishedAt,
    channelId = channelId,
    title = title,
    description = description,
    thumbnail = thumbnail,
    channelTitle = channelTitle,
    playListId = playListId,
    position = position,
    resource = resource.toEntity(),
    videoOwnerChannelId = videoOwnerChannelId,
    videoOwnerChannelTitle = videoOwnerChannelTitle,
    startSeconds = startSeconds,
)

fun Localized.toEntity() = LocalizedEntity(
    title = title,
    description = description,
)

fun Resource.toEntity() = ResourceEntity(
    kind = kind,
    videoId = videoId,
)

fun ContentDetails.toEntity() = ContentDetailsEntity(
    itemCount = itemCount,
)