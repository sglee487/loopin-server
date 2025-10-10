package com.loopin.streaming_service.domain.web.dto

import com.loopin.streaming_service.domain.model.StreamStatus
import java.time.Instant

data class StreamerResponseDto(
    val id: Long,
    val publicId: String,
    val streamKey: String,  // OBS Stream Key 필드에 입력
    val rtmpUrl: String,    // OBS Server 필드에 입력 (예: rtmp://host:1935/live)
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