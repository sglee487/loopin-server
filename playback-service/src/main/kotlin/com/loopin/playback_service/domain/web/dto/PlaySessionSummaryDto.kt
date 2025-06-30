package com.loopin.playback_service.domain.web.dto

import java.time.Instant

data class PlaySessionSummaryDto(
    val id: Long,
    val userId: String,
    val startSeconds: Int,
    val playlist: MediaPlaylistDto,
    val nowPlaying: MediaItemDto,
    val createdAt: Instant?,
    val updatedAt: Instant?
)