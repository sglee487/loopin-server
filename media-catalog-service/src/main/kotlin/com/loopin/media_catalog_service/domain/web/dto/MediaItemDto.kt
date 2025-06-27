package com.loopin.media_catalog_service.domain.web.dto

import java.time.Instant

data class MediaItemDto(
    val id: Long,
    val resourceId: String,
    val title: String,
    val description: String?,
    val kind: String,
    val publishedAt: Instant,
    val thumbnail: String?,
    val channelId: String?,
    val channelTitle: String?,
    val platformType: String,
    val durationSeconds: Long,
    val position: Int? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
