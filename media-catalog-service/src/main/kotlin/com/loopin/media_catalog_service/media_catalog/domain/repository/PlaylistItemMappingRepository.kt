package com.loopin.media_catalog_service.media_catalog.domain.repository

import com.loopin.media_catalog_service.media_catalog.domain.model.PlaylistItemMapping
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface PlaylistItemMappingRepository: ReactiveCrudRepository<PlaylistItemMapping, Long> {
    fun findByPlaylistIdAndMediaItemId(playlistId: Long, mediaItemId: Long): Mono<PlaylistItemMapping?>
}