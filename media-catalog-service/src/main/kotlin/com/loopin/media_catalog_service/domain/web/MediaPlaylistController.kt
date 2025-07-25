package com.loopin.media_catalog_service.domain.web

import com.loopin.media_catalog_service.domain.model.MediaPlaylist
import com.loopin.media_catalog_service.domain.service.MediaPlaylistService
import com.loopin.media_catalog_service.domain.web.dto.CreatePlaylistRequestDto
import com.loopin.media_catalog_service.domain.web.dto.IdListDto
import com.loopin.media_catalog_service.domain.web.dto.PlaylistResponseDto
import com.loopin.media_catalog_service.domain.web.dto.SliceResponse
import com.loopin.media_catalog_service.domain.web.mapper.toDto
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/playlists")
class MediaPlaylistController(
    private val svc: MediaPlaylistService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun getSlice(
        @RequestParam size: Int = 20,
        @RequestParam sortBy: String = "createdAt",
        @RequestParam direction: String = "DESC",
        @RequestParam offset: Long = 0L,
    ): Mono<SliceResponse<MediaPlaylist>> =
        svc.getSlice(
            size = size,
            sortBy = sortBy,
            direction = direction,
            offset = offset,
        ).map {
            SliceResponse(
                items = it.content,
                hasNext = it.hasNext(),
            )
        }

    /** 내부 PK로 조회 */
    @GetMapping("/{id}")
    fun getByIdWithItems(@PathVariable id: Long): Mono<PlaylistResponseDto> =
        svc.getByIdWithItems(id)

    @PostMapping("/batch")
    fun getListsBatch(@RequestBody ids: IdListDto): Flux<PlaylistResponseDto> =
        svc.findAllById(ids.ids)
            .map {
                it.toDto(
                    items = emptyList()
                )
            }

    /** YouTube ID로 조회 */
    @GetMapping("/youtube/{resourceId}")
    fun getByResourceId(@PathVariable resourceId: String): Mono<MediaPlaylist> =
        svc.getByResourceId(resourceId)

    /** YouTube ID 기반 신규 생성 */
    @PostMapping("/youtube")
    fun createFromYoutube(@RequestBody req: CreatePlaylistRequestDto): Mono<MediaPlaylist> =
        svc.createByResourceId(req.resourceId)

    /** YouTube ID 기반 업데이트 */
    @PatchMapping("/youtube/{resourceId}")
    fun updateFromYoutube(@PathVariable resourceId: String): Mono<MediaPlaylist> =
        svc.updateByResourceId(resourceId)
}