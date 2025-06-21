package com.loopin.media_catalog_service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("media_playlist")
data class MediaPlaylist(
    @Id val id: Long? = null,
    @Column("resource_id") val resourceId: String,
    @Column("title") val title: String,
    @Column("description") val description: String?,
    @Column("kind") val kind: String,
    @Column("thumbnail") val thumbnail: String?,
    @Column("channel_id") val channelId: String,
    @Column("channel_title") val channelTitle: String,
    @Column("published_at") val publishedAt: Instant,
    @Column("platform_type") val platformType: String,

    @Column("item_count") val itemCount: Int,

    override var createdAt: Instant? = null,
    override var updatedAt: Instant? = null,
    override var createdBy: String? = null,
    override var updatedBy: String? = null,
) : AuditTimeUser
