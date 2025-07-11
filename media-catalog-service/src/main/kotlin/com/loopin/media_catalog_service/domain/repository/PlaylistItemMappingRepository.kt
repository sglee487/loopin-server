package com.loopin.media_catalog_service.domain.repository

import com.loopin.media_catalog_service.domain.model.PlaylistItemMapping
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PlaylistItemMappingRepository: ReactiveCrudRepository<PlaylistItemMapping, Long> {
    fun findByPlaylistIdAndMediaItemId(playlistId: Long, mediaItemId: Long): Mono<PlaylistItemMapping?>
    fun findByPlaylistId(playlistId: Long): Flux<PlaylistItemMapping>
    fun deleteByPlaylistIdAndMediaItemId(playlistId: Long, mediaItemId: Long): Mono<Void>
}