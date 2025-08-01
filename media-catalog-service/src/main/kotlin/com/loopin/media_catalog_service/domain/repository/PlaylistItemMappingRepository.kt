package com.loopin.media_catalog_service.domain.repository

import com.loopin.media_catalog_service.domain.model.PlaylistItemMapping
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PlaylistItemMappingRepository : ReactiveCrudRepository<PlaylistItemMapping, Long> {
    fun findByPlaylistIdAndMediaItemId(playlistId: Long, mediaItemId: Long): Mono<PlaylistItemMapping?>
    fun findByPlaylistId(playlistId: Long): Flux<PlaylistItemMapping>
    fun deleteByPlaylistIdAndMediaItemId(playlistId: Long, mediaItemId: Long): Mono<Void>
    fun findFirstByPlaylistIdOrderByRankKeyAsc(playlistId: Long): Mono<PlaylistItemMapping>

    @Modifying
    @Query(
        """
        INSERT INTO playlist_item_mapping
               (playlist_id, media_item_id, rank_key, created_at, updated_at)
        VALUES (:playlistId, :mediaItemId, :rankKey, NOW(), NOW())
        ON CONFLICT (playlist_id, media_item_id)
        DO UPDATE SET rank_key   = :rankKey,
                      updated_at = NOW()
        """
    )
    fun upsertRankKey(
        playlistId: Long,
        mediaItemId: Long,
        rankKey: String,
    ): Mono<Int>    // 변경된 행 수
}