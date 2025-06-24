package com.loopin.media_catalog_service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("media_item")
data class MediaItem(
    @Id val id: Long? = null,
    @Column("resource_id") val resourceId: String,
    @Column("title") val title: String,
    @Column("description") val description: String?,
    @Column("kind") val kind: String,
    @Column("published_at") val publishedAt: Instant,
    @Column("thumbnail") val thumbnail: String?,
    @Column("video_owner_channel_id") val videoOwnerChannelId: String?,
    @Column("video_owner_channel_title") val videoOwnerChannelTitle: String?,
    @Column("platform_type") val platformType: String,
    @Column("duration_seconds") val durationSeconds: Long,

    override var createdAt: Instant? = null,
    override var updatedAt: Instant? = null,
    override var createdBy: String? = null,
    override var updatedBy: String? = null,
) : AuditTimeUser