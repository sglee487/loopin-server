package com.loopin.youtube_fetcher_service.media_catalog

import java.time.Instant

data class MediaPlaylist(
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
)
