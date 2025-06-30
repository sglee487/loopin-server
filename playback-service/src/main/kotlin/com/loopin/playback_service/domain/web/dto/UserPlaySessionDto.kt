package com.loopin.playback_service.domain.web.dto

import com.loopin.playback_service.media_catalog.MediaItem
import com.loopin.playback_service.media_catalog.MediaPlaylist
import java.time.Instant

data class UserPlaySessionDto(
    val id: Long,
    val userId: String,
    val startSeconds: Int,
    val playlist: MediaPlaylist,      // 간단히 재사용
    val nowPlaying: MediaItem,
    val prevItems: List<MediaItem>,
    val nextItems: List<MediaItem>,
    val createdAt: Instant?,
    val updatedAt: Instant?
)