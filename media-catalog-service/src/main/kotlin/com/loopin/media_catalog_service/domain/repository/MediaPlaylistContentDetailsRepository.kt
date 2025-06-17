package com.loopin.media_catalog_service.domain.repository

import com.loopin.media_catalog_service.domain.model.MediaPlaylistContentDetails
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface MediaPlaylistContentDetailsRepository: ReactiveCrudRepository<MediaPlaylistContentDetails, Long> {
    fun findByMediaPlaylistId(mediaPlaylistId: Long): Mono<MediaPlaylistContentDetails>
}