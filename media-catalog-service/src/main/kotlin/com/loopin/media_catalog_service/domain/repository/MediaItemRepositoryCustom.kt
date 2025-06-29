package com.loopin.media_catalog_service.domain.repository

import com.loopin.media_catalog_service.domain.model.MediaItem
import reactor.core.publisher.Flux

interface MediaItemRepositoryCustom {
    /**
     * 입력한 id 리스트의 **순서를 그대로** 보존해 MediaItem을 반환한다.
     * ids 가 비어 있으면 Flux.empty()
     */
    fun findAllByIdPreserveOrder(ids: List<Long>): Flux<MediaItem>
}