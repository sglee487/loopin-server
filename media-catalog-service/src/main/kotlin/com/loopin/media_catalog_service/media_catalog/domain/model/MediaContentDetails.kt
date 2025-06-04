package com.loopin.media_catalog_service.media_catalog.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("media_content_details")
data class MediaContentDetails(
    @Column("media_item_id") @Id val mediaItemId: String,
    val duration: Int,
    val caption: Boolean,
)
