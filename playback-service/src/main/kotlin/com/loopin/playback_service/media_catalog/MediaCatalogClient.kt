package com.loopin.playback_service.media_catalog

import com.loopin.playback_service.config.WebClientFactory
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MediaCatalogClient(
    factory: WebClientFactory
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val webClient = factory.mediaCatalogServiceClient()

    /** 플레이리스트 여러 개 */
    fun getMediaPlaylistBatch(ids: List<Long>) =
        webClient.post()
            .uri("/api/v1/playlists/batch")
            .bodyValue(ids)
            .retrieve()
            .bodyToFlux(MediaPlaylist::class.java)   // 배열 수신
            .collectList()                           // Mono<List<MediaPlaylist>>
            .doOnError { logger.error("Failed to fetch playlists", it) }

    /** 미디어 아이템 여러 개 */
    fun getMediaItemBatch(ids: List<Long>) =
        webClient.post()
            .uri("/api/v1/items/batch")
            .bodyValue(ids)
            .retrieve()
            .bodyToFlux(MediaItem::class.java)       // 배열 수신
            .collectList()                           // Mono<List<MediaItem>>
            .doOnError { logger.error("Failed to fetch items", it) }
}