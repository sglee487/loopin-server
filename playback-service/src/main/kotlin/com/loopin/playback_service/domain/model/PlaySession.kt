package com.loopin.playback_service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class PlaySession(
    @Id val id: Long? = null,
    @Column("user_id") val userId: String,
    @Column("media_playlist_id") val mediaPlaylistId: Long,
    @Column("now_playing_item_id") val nowPlayingItemId: Long,
    @Column("start_seconds") val startSeconds: Int = 0,
    @Column("prev_items") val prevItems: List<Long> = emptyList(),
    @Column("next_items") val nextItems: List<Long> = emptyList(),

    override var createdAt: Instant? = null,
    override var updatedAt: Instant? = null,
    override var createdBy: String? = null,
    override var updatedBy: String? = null,
): AuditTimeUser