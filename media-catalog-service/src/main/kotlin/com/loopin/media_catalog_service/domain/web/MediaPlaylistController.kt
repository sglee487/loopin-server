package com.loopin.media_catalog_service.domain.web

import com.loopin.media_catalog_service.domain.model.MediaPlaylist
import com.loopin.media_catalog_service.domain.service.MediaPlaylistService
import com.loopin.media_catalog_service.domain.web.dto.CreatePlaylistRequestDto
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/playlists")
class MediaPlaylistController(
    private val svc: MediaPlaylistService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /** 내부 PK로 조회 */
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Mono<MediaPlaylist> =
        svc.getById(id)

    /** YouTube ID로 조회 */
    @GetMapping("/youtube/{resourceId}")
    fun getByResourceId(@PathVariable resourceId: String): Mono<MediaPlaylist> =
        svc.getByResourceId(resourceId)

    /** YouTube ID 기반 신규 생성 */
    @PostMapping("/youtube")
    fun createFromYoutube(@RequestBody req: CreatePlaylistRequestDto): Mono<MediaPlaylist> =
        svc.createByResourceId(req.resourceId)

}