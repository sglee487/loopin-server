package com.loopin.playback_service.media_catalog

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
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
)