package com.loopin.media_catalog_service.domain.repository

import com.loopin.media_catalog_service.domain.model.MediaPlaylist
import org.springframework.data.domain.Slice
import reactor.core.publisher.Mono

interface MediaPlaylistRepositoryCustom {
    fun findAllBy(
        size: Int,
        sortBy: String,
        direction: String,
        offset: Long,
    ): Mono<Slice<MediaPlaylist>>
}