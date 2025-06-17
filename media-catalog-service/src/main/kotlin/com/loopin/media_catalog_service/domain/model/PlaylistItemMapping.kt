package com.loopin.media_catalog_service.domain.model

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("playlist_item_mapping")
data class PlaylistItemMapping(
    val id: Long? = null,

    @Column("playlist_id") val playlistId: Long,
    @Column("media_item_id") val mediaItemId: Long,

    val position: Int,

    override var createdAt: Instant? = null,
    override var updatedAt: Instant? = null,
): AuditTime