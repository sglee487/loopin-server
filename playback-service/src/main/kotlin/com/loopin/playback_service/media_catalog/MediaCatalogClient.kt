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

    fun getMediaPlaylist(playlistId: String) = webClient.get()
        .uri("/playlist/{id}", playlistId)
        .retrieve()
        .bodyToMono(String::class.java)
        .doOnError { logger.error("Failed to fetch playlist: $it") }
}