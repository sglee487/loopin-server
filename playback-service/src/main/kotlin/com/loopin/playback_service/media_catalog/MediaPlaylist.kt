package com.loopin.playback_service.media_catalog

import java.time.Instant

data class MediaPlaylist(
    val id: Long? = null,
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

    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
    var createdBy: String? = null,
    var updatedBy: String? = null,
)