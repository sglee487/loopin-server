package com.loopin.media_catalog_service.media_catalog.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("media_playlist_content_details")
data class MediaPlaylistContentDetails(
    @Id @Column("media_playlist_id") val mediaPlaylistId: String,
    @Column("item_count") val itemCount: Int,
)
