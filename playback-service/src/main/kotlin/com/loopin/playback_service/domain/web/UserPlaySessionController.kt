package com.loopin.playback_service.domain.web

import com.loopin.playback_service.domain.service.UserPlaySessionService
import com.loopin.playback_service.domain.web.dto.PutUserPlaySessionRequestDto
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

    /** 세션 전체 목록 */
    @GetMapping("/sessions")
    fun getUserPlaySessions(principal: JwtAuthenticationToken): Flux<UserPlaySessionDto> =
        svc.getUserPlaySessions(principal.name)

    /** 세션 1건 + 큐 포함 조회 */
    @GetMapping("/sessions/{playlistId}")
    fun getUserPlaySessionByPlaylistId(
        principal: JwtAuthenticationToken,
        @PathVariable playlistId: Long,
    ): Mono<UserPlaySessionDto> =
        svc.getUserPlaySessionByPlaylistIdWithItems(principal.name, playlistId)

    /** ▶️  Upsert (없으면 생성, 있으면 전부 덮어쓰기)  */
    @PutMapping("/sessions/{playlistId}")
    fun putUserPlaySession(
        principal: JwtAuthenticationToken,
        @PathVariable playlistId: Long,
        @RequestBody req: PutUserPlaySessionRequestDto,   // ← 새 DTO 하나로 통일
    ): Mono<Void> =
        svc.upsertUserPlaySession(
            playSession = req.toDomain(
                userId = principal.name,
                mediaPlaylistId = playlistId,
            )
        )
}
