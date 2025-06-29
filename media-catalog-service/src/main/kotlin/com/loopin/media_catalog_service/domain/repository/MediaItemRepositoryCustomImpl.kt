package com.loopin.media_catalog_service.domain.repository

import com.loopin.media_catalog_service.domain.model.MediaItem
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository // 주입용
class MediaItemRepositoryCustomImpl(
    private val client: DatabaseClient,
) : MediaItemRepositoryCustom {

    override fun findAllByIdPreserveOrder(ids: List<Long>): Flux<MediaItem> {
        if (ids.isEmpty()) return Flux.empty()

        val sql = """
            SELECT mi.*
            FROM unnest(:ids) WITH ORDINALITY AS u(id, ord)
            JOIN media_item mi ON mi.id = u.id
            ORDER BY u.ord
        """.trimIndent()

        return client.sql(sql)
            .bind("ids", ids.toTypedArray())   // bigint[]
            .map(::mapRow)
            .all()
    }

    private fun mapRow(row: Row, meta: RowMetadata): MediaItem =
        MediaItem(
            id = row.get("id", java.lang.Long::class.java)?.toLong(),
            resourceId = row.get("resource_id", String::class.java)!!,
            title = row.get("title", String::class.java)!!,
            description = row.get("description", String::class.java),
            kind = row.get("kind", String::class.java)!!,
            publishedAt = row.get("published_at", java.time.Instant::class.java)!!,
            thumbnail = row.get("thumbnail", String::class.java),
            videoOwnerChannelId = row.get("video_owner_channel_id", String::class.java),
            videoOwnerChannelTitle = row.get("video_owner_channel_title", String::class.java),
            platformType = row.get("platform_type", String::class.java)!!,
            durationSeconds = row.get("duration_seconds", java.lang.Long::class.java)!!.toLong(),
            createdAt = row.get("created_at", java.time.Instant::class.java),
            updatedAt = row.get("updated_at", java.time.Instant::class.java),
            createdBy = row.get("created_by", String::class.java),
            updatedBy = row.get("updated_by", String::class.java),
        )
}