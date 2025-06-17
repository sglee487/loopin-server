package com.loopin.playback_service.domain.web

import com.loopin.playback_service.domain.model.PlaySession
import com.loopin.playback_service.domain.service.UserPlaySessionService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.nio.file.attribute.UserPrincipal

@RestController
@RequestMapping("/api/v1/user-play-session")
class UserPlaySessionController(
    private val svc: UserPlaySessionService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/users/{userId}/sessions")
    fun getUserPlaySession(
        principal: UserPrincipal,
        @PathVariable userId: String,
    ): Flux<PlaySession> {
        logger.info(principal.toString())
        if (principal.name != userId) throw IllegalArgumentException("Invalid user id")

        return svc.getUserPlaySessions(userId)
            .doOnNext { logger.info(it.toString()) } // 단순 로그만 찍고 싶을 땐 flatMap 대신 doOnNext
    }
}