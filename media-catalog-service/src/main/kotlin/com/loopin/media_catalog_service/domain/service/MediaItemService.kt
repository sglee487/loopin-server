package com.loopin.media_catalog_service.domain.service

import com.loopin.media_catalog_service.domain.model.MediaItem
import com.loopin.media_catalog_service.domain.repository.MediaItemRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class MediaItemService(
    private val mediaItemRepository: MediaItemRepository
) {

    fun findAllByIdPreserveOrder(ids: List<Long>): Flux<MediaItem> = mediaItemRepository.findAllByIdPreserveOrder(ids)
}