package com.loopin.playback_service.domain.web.dto

import java.time.Instant

data class PlaySessionDetailDto(
    val id: Long,
    val userId: String,
    val startSeconds: Int,
    val playlist: MediaPlaylistDto,
    val nowPlaying: MediaItemDto,
    val prevItems: List<MediaItemDto>,
    val nextItems: List<MediaItemDto>,
    val createdAt: Instant?,
    val updatedAt: Instant?
)
