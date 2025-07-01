package com.loopin.playback_service.domain.web.mapper

import com.loopin.playback_service.domain.model.PlaySession
import com.loopin.playback_service.domain.web.dto.*
import com.loopin.playback_service.media_catalog.MediaItem
import com.loopin.playback_service.media_catalog.MediaPlaylist

fun PlaySession.toSummaryDto(
    playlist: MediaPlaylistDto,
    now: MediaItemDto
) = PlaySessionSummaryDto(
    id = id!!,
    userId = userId,
    startSeconds = startSeconds,
    playlist = playlist,
    nowPlaying = now,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun PlaySession.toDetailDto(
    playlist: MediaPlaylistDto,
    now: MediaItemDto,
    prev: List<MediaItemDto>,
    next: List<MediaItemDto>
) = PlaySessionDetailDto(
    id = id!!,
    userId = userId,
    startSeconds = startSeconds,
    playlist = playlist,
    nowPlaying = now,
    prevItems = prev,
    nextItems = next,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun PlaySession.toDto(
    playlist: MediaPlaylist,
    now: MediaItem,
    prev: List<MediaItem>?,
    next: List<MediaItem>?,
    prevItemsLength: Int,
    nextItemsLength: Int,
) = UserPlaySessionDto(
    id           = id!!,
    userId       = userId,
    startSeconds = startSeconds,
    playlist     = playlist,
    nowPlaying   = now,
    prevItems    = prev,
    nextItems    = next,
    prevItemsLength = prevItemsLength,
    nextItemsLength = nextItemsLength,
    createdAt    = createdAt,
    updatedAt    = updatedAt
)