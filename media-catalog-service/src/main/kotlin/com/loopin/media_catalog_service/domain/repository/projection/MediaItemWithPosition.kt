package com.loopin.media_catalog_service.domain.repository.projection

import java.time.Instant

data class MediaItemWithPosition(
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
    val playlistPosition: Int?
)
