package com.loopin.youtube_fetcher_service.media_catalog

import java.time.Instant

data class MediaItem(
    val id: Long? = null,
    val resourceId: String,
    val publishedAt: Instant,
    val uploaderChannelId: String,
    val thumbnail: String?,
    val videoOwnerChannelId: String?,
    val videoOwnerChannelTitle: String?,
    val platformType: String,
)
