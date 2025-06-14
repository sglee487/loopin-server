package com.loopin.youtube_fetcher_service.web

import com.loopin.youtube_fetcher_service.domain.YoutubeFetcherService
import com.loopin.youtube_fetcher_service.media_catalog.MediaItem
import com.loopin.youtube_fetcher_service.media_catalog.MediaPlaylist
import com.loopin.youtube_fetcher_service.media_catalog.PlaylistWithItems
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class YoutubeFetcherController(
    private val svc: YoutubeFetcherService
) {

    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    @GetMapping("/playlists/{id}")
    fun getPlaylists(
        @PathVariable id: String,
    ): Mono<PlaylistWithItems> {
        logger.info("Fetching playlist with id: $id")
        return svc.getMediaPlaylist(id)
    }

    @GetMapping("/item/{id}")
    fun getPlayItem(
        @PathVariable id: String,
    ): Mono<MediaItem> {
        return svc.getMediaItem(id)
    }
}