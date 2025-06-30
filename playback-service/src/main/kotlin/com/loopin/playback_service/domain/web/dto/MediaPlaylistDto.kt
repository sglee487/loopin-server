package com.loopin.playback_service.domain.web.dto

import java.time.Instant

data class MediaPlaylistDto(
    val id: Long,
    val resourceId: String,
    val title: String,
    val description: String?,
    val kind: String,
    val thumbnail: String?,
    val channelId: String,
    val channelTitle: String,
    val publishedAt: Instant,
    val platformType: String,
    val itemCount: Int,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val createdBy: String?,
    val updatedBy: String?,
    val items: List<MediaItemDto>,
)