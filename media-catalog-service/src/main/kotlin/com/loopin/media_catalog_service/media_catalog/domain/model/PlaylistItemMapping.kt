package com.loopin.media_catalog_service.media_catalog.domain.model

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("playlist_item_mapping")
data class PlaylistItemMapping(
    @Column("playlist_id") val playlistId: String,
    @Column("media_item_id") val mediaItemId: String,
    val position: Int
)
