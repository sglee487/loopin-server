package com.loopin.media_catalog_service.youtube

import com.loopin.media_catalog_service.config.WebClientFactory
import com.loopin.media_catalog_service.domain.model.MediaItem
import com.loopin.media_catalog_service.domain.model.PlaylistWithItems
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.io.IOException
import java.time.Duration

@Component
class YoutubeClient(factory: WebClientFactory) {

    private val logger = org.slf4j.LoggerFactory.getLogger(javaClass)

    private val webClient: WebClient = factory.youtubeClient()

    fun getPlaylistWithItems(playlistId: String): Mono<PlaylistWithItems> {
        return webClient
            .get()
            .uri("/playlists/{id}", playlistId)
            .retrieve()
            .bodyToMono(PlaylistWithItems::class.java)
            .timeout(Duration.ofSeconds(3), Mono.empty())
            .retryWhen(
                Retry.backoff(3, Duration.ofMillis(100))
                    .doBeforeRetry { retrySignal ->
                        logger.warn(
                            "Retrying playlist fetch. Attempt: {}, due to: {}",
                            retrySignal.totalRetries() + 1,
                            retrySignal.failure().message
                        )
                    }
            )
            .onErrorResume(WebClientResponseException.NotFound::class.java) { Mono.empty() }
            .onErrorResume(IOException::class.java) { Mono.empty() }
            .onErrorResume { t ->
                logger.warn("Unhandled error during playlist fetch: {}", t.message)
                Mono.empty()
            }
    }

    fun getMediaItem(mediaItemId: String): Mono<MediaItem> {
        return webClient
            .get()
            .uri("/items/{id}", mediaItemId)
            .retrieve()
            .bodyToMono(MediaItem::class.java)
            .timeout(Duration.ofSeconds(3), Mono.empty())
            .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
            .onErrorResume(WebClientResponseException.NotFound::class.java) { Mono.empty() }
            .onErrorResume(IOException::class.java) { Mono.empty() }
            .onErrorResume { t -> logger.warn("Unhandled error: {}", t); Mono.empty() }
    }
}
