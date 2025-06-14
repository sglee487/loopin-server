package com.loopin.media_catalog_service.media_catalog.domain.repository

import com.loopin.media_catalog_service.media_catalog.domain.model.MediaPlaylist
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface MediaPlaylistRepository: ReactiveCrudRepository<MediaPlaylist, Long> {
    fun findByResourceId(resourceId: String): Mono<MediaPlaylist>
}