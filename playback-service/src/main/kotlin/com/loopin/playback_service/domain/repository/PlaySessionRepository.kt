package com.loopin.playback_service.domain.repository

import com.loopin.playback_service.domain.model.PlaySession
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface PlaySessionRepository: ReactiveCrudRepository<PlaySession, Long> {
    fun findAllByUserId(userId: String): Flux<PlaySession>
}