package com.loopin.playback_service.media_catalog

import com.loopin.playback_service.config.WebClientFactory
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class MediaCatalogClient(
    factory: WebClientFactory
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val webClient = factory.mediaCatalogServiceClient()

    /** 플레이리스트 여러 개 */
    fun getMediaPlaylistBatch(ids: List<Long>): Mono<List<MediaPlaylist>> =
        webClient.post()
            .uri("/api/v1/playlists/batch")
            .bodyValue(IdsRequest(ids))              // ✅ 래퍼 객체
            .retrieve()
            .bodyToFlux(MediaPlaylist::class.java)
            .collectList()
            .doOnError { logger.error("Failed to fetch playlists", it) }

    /** 미디어 아이템 여러 개 */
    fun getMediaItemBatch(ids: List<Long>): Mono<List<MediaItem>> =
        webClient.post()
            .uri("/api/v1/items/batch")
            .bodyValue(IdsRequest(ids))              // ✅ 래퍼 객체
            .retrieve()
            .bodyToFlux(MediaItem::class.java)
            .collectList()
            .doOnError { logger.error("Failed to fetch items", it) }
}

private data class IdsRequest(val ids: List<Long>)