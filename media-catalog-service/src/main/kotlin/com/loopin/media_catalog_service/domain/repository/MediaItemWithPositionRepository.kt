package com.loopin.media_catalog_service.domain.repository

import com.loopin.media_catalog_service.domain.repository.projection.MediaItemWithPosition
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface MediaItemWithPositionRepository : R2dbcRepository<MediaItemWithPosition, Long> {
    @Query(
        """
    SELECT mi.*,
           pim.position AS playlist_position   -- snake_case 그대로
      FROM playlist_item_mapping pim
      JOIN media_item mi ON mi.id = pim.media_item_id
     WHERE pim.playlist_id = :playlistId
     ORDER BY pim.position
    """
    )
    fun findByPlaylistId(playlistId: Long): Flux<MediaItemWithPosition>
}