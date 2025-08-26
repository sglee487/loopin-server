package com.loopin.streaming_service.domain.repository

import com.loopin.streaming_service.domain.model.VideoSegment
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface VideoSegmentRepository : R2dbcRepository<VideoSegment, Long> {
    fun findByStreamSessionIdOrderBySegmentNumber(streamSessionId: Long): Flux<VideoSegment>
    fun findByStreamSessionIdAndResolution(streamSessionId: Long, resolution: String): Flux<VideoSegment>
}