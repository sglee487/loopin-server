package com.loopin.media_catalog_service.media_catalog.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("media_playlist_localization")
data class MediaPlaylistLocalization(
    @Id @Column("media_playlist_id") val mediaPlaylistId: String,
    @Column("language_code") val languageCode: String,
    val title: String,
    val description: String?
)
