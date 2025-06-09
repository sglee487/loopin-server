package com.loopin.youtube_fetcher_service.domain

import com.loopin.youtube_fetcher_service.infrastructure.YoutubeApiClient
import com.loopin.youtube_fetcher_service.media_catalog.MediaItem
import com.loopin.youtube_fetcher_service.media_catalog.MediaPlaylist
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class YoutubeFetcherService(
    private val youtubeApiClient: YoutubeApiClient
) {
    fun getMediaPlaylist(id: String): Mono<MediaPlaylist> {
        return youtubeApiClient.getMediaPlaylist(
            id = id,
        )
    }

    fun getMediaItem(id: String): Mono<MediaItem> {
        return youtubeApiClient.getMediaItem(
            id = id,
        )
    }
}