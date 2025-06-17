package com.loopin.media_catalog_service.domain.repository

import com.loopin.media_catalog_service.domain.model.MediaItem
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface MediaItemRepository : ReactiveCrudRepository<MediaItem, Long> {
    fun findByResourceId(resourceId: String): Mono<MediaItem> // nullable 제거
}