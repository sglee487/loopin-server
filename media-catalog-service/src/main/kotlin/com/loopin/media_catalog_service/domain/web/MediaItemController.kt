package com.loopin.media_catalog_service.domain.web

import com.loopin.media_catalog_service.domain.service.MediaItemService
import com.loopin.media_catalog_service.domain.web.dto.IdListDto
import com.loopin.media_catalog_service.domain.web.dto.MediaItemDto
import com.loopin.media_catalog_service.domain.web.mapper.toDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/v1/items")
class MediaItemController(
    private val svc: MediaItemService,
) {

    companion object { private const val MAX_BATCH = 300 }

    /** 300개까지 id 배열을 받아 MediaItemDto 리스트를 순서대로 돌려준다 */
    @PostMapping("/batch")
    fun getItemsBatch(@RequestBody ids: IdListDto): Flux<MediaItemDto> {
        if (ids.ids.size > MAX_BATCH) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "ids size must be ≤ $MAX_BATCH (given ${ids.ids.size})"
            )
        }

        return svc.findAllByIdPreserveOrder(ids.ids)
            .map { it.toDto() }
    }
}