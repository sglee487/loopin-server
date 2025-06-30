package com.loopin.playback_service.media_catalog

import java.time.Instant

data class MediaItem(
    val id: Long? = null,
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

    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
    var createdBy: String? = null,
    var updatedBy: String? = null,
)