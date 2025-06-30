package com.loopin.playback_service.domain.web

import com.loopin.playback_service.domain.service.UserPlaySessionService
import com.loopin.playback_service.domain.web.dto.CreateUserPlaySessionRequestDto
import com.loopin.playback_service.domain.web.dto.UpdateUserPlaySessionRequestDto
import com.loopin.playback_service.domain.web.dto.UserPlaySessionDto
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/user-play-session")
class UserPlaySessionController(
    private val svc: UserPlaySessionService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** 세션 목록 */
    @GetMapping("/sessions")
    fun getUserPlaySessions(
        principal: JwtAuthenticationToken,
    ): Flux<UserPlaySessionDto> {
        logger.info("principal: $principal")
        logger.info("principal.name: ${principal.name}")
        return svc.getUserPlaySessions(principal.name)
            .doOnNext { logger.info(it.toString()) }
    }

    /** 특정 플레이리스트 세션(큐 포함) */
    @GetMapping("/sessions/{playlistId}")
    fun getUserPlaySessionByPlaylistIdWithItems(
        principal: JwtAuthenticationToken,
        @PathVariable playlistId: Long,
    ): Mono<UserPlaySessionDto> {
        logger.info("principal: $principal")
        logger.info("principal.name: ${principal.name}")
        return svc.getUserPlaySessionByPlaylistIdWithItems(principal.name, playlistId)
            .doOnNext { logger.info(it.toString()) }
    }

    /** 특정 플레이리스트 세션(큐 포함) */
    @PostMapping("/sessions/{playlistId}")
    fun createUserPlaySessionByPlaylistId(
        principal: JwtAuthenticationToken,
        @PathVariable playlistId: Long,
        @RequestBody req: CreateUserPlaySessionRequestDto,
    ): Mono<Void> {
        return svc.createUserPlaySessionByPlaylistId(
            playSession = req.toDomain(
                userId = principal.name,
                mediaPlaylistId = playlistId,
            )
        )
    }

    @PatchMapping("/sessions/{playlistId}")
    fun updateUserPlaySessionByPlaylistId(
        principal: JwtAuthenticationToken,
        @PathVariable playlistId: Long,
        @RequestBody req: UpdateUserPlaySessionRequestDto,
    ): Mono<Void> {
        return svc.updateUserPlaySessionByPlaylistId(
            playSession = req.toDomain(
                userId = principal.name,
                mediaPlaylistId = playlistId,
            )
        )
    }
}