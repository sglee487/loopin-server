package com.loopin.media_catalog_service.domain.repository

import com.loopin.media_catalog_service.domain.model.MediaPlaylist
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface MediaPlaylistRepository: ReactiveCrudRepository<MediaPlaylist, Long> {
    fun findByResourceId(resourceId: String): Mono<MediaPlaylist>
}