package com.loopin.media_catalog_service.media_catalog.domain.web

import com.loopin.media_catalog_service.media_catalog.domain.model.MediaPlaylist
import com.loopin.media_catalog_service.media_catalog.domain.service.MediaPlaylistService
import com.loopin.media_catalog_service.media_catalog.domain.web.dto.CreatePlaylistRequestDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController()
class MediaPlaylistController(
    private val svc: MediaPlaylistService
) {
    @GetMapping("/playlists")
    fun getPlaylist(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) resourceId: String?
    ): Mono<MediaPlaylist> {
        return when {
            id != null -> svc.getById(id)
            resourceId != null -> svc.getByResourceId(resourceId)
            else -> Mono.error(IllegalArgumentException("Either id or youtubeId must be provided"))
        }
    }

    @PostMapping("/playlists")
    fun createPlaylist(
        @RequestBody req: CreatePlaylistRequestDto,
    ): Mono<MediaPlaylist> {
        return when (req.type) {
            "youtube" -> svc.createByResourceId(resourceId = req.resourceId)
            else -> Mono.error(IllegalArgumentException("Either id or youtubeId must be provided"))
        }
    }
}