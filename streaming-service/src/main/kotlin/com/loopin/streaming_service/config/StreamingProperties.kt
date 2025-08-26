package com.loopin.streaming_service.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "streaming")
data class StreamingProperties(
    val storage: StorageProperties = StorageProperties(),
    val encoding: EncodingProperties = EncodingProperties()
)

data class StorageProperties(
    val basePath: String = "/tmp/streams",
    val cleanupInterval: Long = 300000, // 5 minutes
    val segmentRetentionCount: Int = 20
)

data class EncodingProperties(
    val segmentDuration: Int = 6,
    val resolutions: List<ResolutionConfig> = defaultResolutions()
)

data class ResolutionConfig(
    val name: String,
    val width: Int,
    val height: Int,
    val bitrate: Int
)

private fun defaultResolutions(): List<ResolutionConfig> {
    return listOf(
        ResolutionConfig("240p", 426, 240, 400000),
        ResolutionConfig("360p", 640, 360, 800000),
        ResolutionConfig("480p", 854, 480, 1200000),
        ResolutionConfig("720p", 1280, 720, 2500000)
    )
}