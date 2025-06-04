package com.loopin.media_catalog_service.media_catalog.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("media_item_localization")
data class MediaItemLocalization(
    @Id @Column("media_item_id") val mediaItemId: String,
    @Column("language_code") val languageCode: String,
    val title: String,
    val description: String?
)
