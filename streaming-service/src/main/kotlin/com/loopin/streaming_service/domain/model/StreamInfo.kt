package com.loopin.streaming_service.domain.model

data class StreamInfo(
    val duration: Double,
    val width: Int,
    val height: Int,
    val frameRate: Double,
    val bitrate: Int
)