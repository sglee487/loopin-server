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
    val videoOwnerChannelId: String?,
    val videoOwnerChannelTitle: String?,
    val platformType: String,
    val durationSeconds: Long,
    val position: Int? = null,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val createdBy: String?,
    val updatedBy: String?,
)
