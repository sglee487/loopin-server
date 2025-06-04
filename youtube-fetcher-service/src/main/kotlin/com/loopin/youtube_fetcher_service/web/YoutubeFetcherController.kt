package com.loopin.youtube_fetcher_service.web

import com.loopin.youtube_fetcher_service.domain.YoutubeFetcherService
import com.loopin.youtube_fetcher_service.media_catalog.MediaPlaylist
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class YoutubeFetcherController(
    private val svc: YoutubeFetcherService
) {
    @GetMapping("/playlists/{id}")
    fun getPlaylists(
        @PathVariable id: String,
    ): Mono<MediaPlaylist> {
        return svc.getMediaPlaylist(id)
    }
}