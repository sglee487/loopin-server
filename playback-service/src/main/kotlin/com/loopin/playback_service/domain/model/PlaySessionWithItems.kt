package com.loopin.playback_service.domain.model

import com.loopin.playback_service.media_catalog.MediaItem
import com.loopin.playback_service.media_catalog.MediaPlaylist
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.time.Instant

data class PlaySessionWithItems(
    val id: Long? = null,
    val userId: String,
    val mediaPlaylistId: MediaItem,
    val nowPlayingItemId: MediaPlaylist,
    val startSeconds: Int = 0,
    val prevItems: List<Long> = emptyList(),
    val nextItems: List<Long> = emptyList(),

    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
    var createdBy: String? = null,
    var updatedBy: String? = null,
) {
}