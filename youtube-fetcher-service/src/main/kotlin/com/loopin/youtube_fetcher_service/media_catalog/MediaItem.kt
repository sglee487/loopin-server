package com.loopin.youtube_fetcher_service.media_catalog

import java.time.Instant

data class MediaItem(
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
    val videoId: String,
)