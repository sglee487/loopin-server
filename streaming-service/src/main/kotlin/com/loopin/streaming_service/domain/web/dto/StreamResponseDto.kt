package com.loopin.streaming_service.domain.web.dto

import com.loopin.streaming_service.domain.model.StreamStatus
import java.time.Instant

data class StreamResponseDto(
    val id: Long,
    val streamKey: String,
    val title: String,
    val description: String?,
    val status: StreamStatus,
    val streamerId: String,
    val resolution: String,
    val bitrate: Int,
    val viewersCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val hlsUrl: String
)