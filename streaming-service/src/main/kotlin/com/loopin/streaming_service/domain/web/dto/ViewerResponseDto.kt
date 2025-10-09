package com.loopin.streaming_service.domain.web.dto

import com.loopin.streaming_service.domain.model.StreamStatus
import java.time.Instant

data class ViewerResponseDto(
    val id: Long,
    val publicId: String,
    val title: String,
    val description: String?,
    val status: StreamStatus,
    val resolution: String,
    val bitrate: Int,
    val viewersCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val hlsUrl: String
)