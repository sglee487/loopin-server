package com.loopin.youtube_fetcher_service.domain

import com.loopin.youtube_fetcher_service.infrastructure.YoutubeApiClient
import com.loopin.youtube_fetcher_service.media_catalog.MediaItem
import com.loopin.youtube_fetcher_service.media_catalog.MediaPlaylist
import com.loopin.youtube_fetcher_service.media_catalog.PlaylistWithItems
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class YoutubeFetcherService(
    private val youtubeApiClient: YoutubeApiClient
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    fun getMediaPlaylist(id: String): Mono<PlaylistWithItems> {

        logger.info("Fetching playlist with id: $id")

        return youtubeApiClient.getMediaPlaylist(
            id = id,
        ).map {
//            logger.info(it.toString())
//            logger.info(it.playlist.title)
//            logger.info(it.mediaItem.first().title)
            it
        }
    }

    fun getMediaItem(id: String): Mono<MediaItem> {
        return youtubeApiClient.getMediaItem(
            id = id,
        )
    }
}