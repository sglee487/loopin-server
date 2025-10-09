package com.loopin.streaming_service.domain.repository

import com.loopin.streaming_service.domain.model.StreamSession
import com.loopin.streaming_service.domain.model.StreamStatus
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface StreamSessionRepository : R2dbcRepository<StreamSession, Long> {
    fun findByStreamKey(streamKey: String): Mono<StreamSession>
    fun findByPublicId(publicId: String): Mono<StreamSession>
    fun findByStreamerId(streamerId: String): Mono<StreamSession>
    fun findByStatus(status: StreamStatus): Flux<StreamSession>
}