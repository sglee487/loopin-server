package com.loopin.youtube_fetcher_service.media_catalog

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("media_item")
data class MediaItem(
    @Id val id: Long? = null,
    @Column("created_at") val createdAt: Instant,
    @Column("uploader_channel_id") val uploaderChannelId: String,
    @Column("thumbnail") val thumbnail: String?,
    @Column("video_owner_channel_id") val videoOwnerChannelId: String?,
    @Column("video_owner_channel_title") val videoOwnerChannelTitle: String?,
    @Column("platform_type") val platformType: String,
)
