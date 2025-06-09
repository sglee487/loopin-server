package com.loopin.youtube_fetcher_service.media_catalog

import java.time.Instant

data class MediaPlaylist(
    val id: Long? = null,
    val resourceId: String,
    val thumbnail: String?,
    val channelId: String,
    val channelTitle: String,
    val publishedAt: Instant,
    val platformType: String,
)
