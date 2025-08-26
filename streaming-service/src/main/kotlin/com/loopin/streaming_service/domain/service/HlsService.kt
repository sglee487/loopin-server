package com.loopin.streaming_service.domain.service

import com.loopin.streaming_service.domain.model.VideoSegment
import com.loopin.streaming_service.domain.repository.VideoSegmentRepository
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.io.File
import java.nio.file.Paths

@Service
class HlsService(
    private val videoSegmentRepository: VideoSegmentRepository
) {
    private val logger = LoggerFactory.getLogger(HlsService::class.java)
    
    companion object {
        private val SUPPORTED_RESOLUTIONS = mapOf(
            "240p" to Pair(426, 240),
            "360p" to Pair(640, 360),
            "480p" to Pair(854, 480),
            "720p" to Pair(1280, 720)
        )
    }

    fun generateMasterPlaylist(streamKey: String, defaultResolution: String): Mono<String> {
        return Mono.fromCallable {
            val playlist = buildString {
                appendLine("#EXTM3U")
                appendLine("#EXT-X-VERSION:3")
                
                SUPPORTED_RESOLUTIONS.forEach { (resolution, dimensions) ->
                    val bitrate = calculateBitrate(dimensions.first, dimensions.second)
                    appendLine("#EXT-X-STREAM-INF:BANDWIDTH=$bitrate,RESOLUTION=${dimensions.first}x${dimensions.second}")
                    appendLine("$resolution/playlist.m3u8")
                }
            }
            playlist
        }
    }

    fun generateResolutionPlaylist(streamKey: String, resolution: String): Mono<String> {
        // For live streams, we need to generate a dynamic playlist
        return getStreamDirectory(streamKey, resolution)
            .map { streamDir ->
                generateLivePlaylist(streamDir, resolution)
            }
    }

    private fun generateLivePlaylist(streamDir: File, resolution: String): String {
        val segmentFiles = streamDir.listFiles { _, name -> 
            name.endsWith(".ts") 
        }?.sortedBy { it.name } ?: emptyList()

        return buildString {
            appendLine("#EXTM3U")
            appendLine("#EXT-X-VERSION:3")
            appendLine("#EXT-X-TARGETDURATION:6")
            
            if (segmentFiles.isNotEmpty()) {
                // For live streams, we show only the last few segments
                val recentSegments = segmentFiles.takeLast(10)
                val firstSegmentNumber = extractSegmentNumber(recentSegments.first().name)
                
                appendLine("#EXT-X-MEDIA-SEQUENCE:$firstSegmentNumber")
                
                recentSegments.forEach { file ->
                    appendLine("#EXTINF:6.0,")
                    appendLine(file.name)
                }
            } else {
                appendLine("#EXT-X-MEDIA-SEQUENCE:0")
            }
            
            // Don't add EXT-X-ENDLIST for live streams
        }
    }

    fun getSegmentFile(streamKey: String, resolution: String, segmentName: String): Mono<Resource> {
        return getStreamDirectory(streamKey, resolution)
            .map { streamDir ->
                val segmentFile = File(streamDir, segmentName)
                if (segmentFile.exists()) {
                    FileSystemResource(segmentFile)
                } else {
                    throw IllegalArgumentException("Segment not found: $streamKey/$resolution/$segmentName")
                }
            }
    }

    private fun getStreamDirectory(streamKey: String, resolution: String): Mono<File> {
        return Mono.fromCallable {
            val streamDir = File(System.getProperty("java.io.tmpdir"), "streams/$streamKey/$resolution")
            if (!streamDir.exists()) {
                streamDir.mkdirs()
            }
            streamDir
        }
    }

    private fun extractSegmentNumber(filename: String): Int {
        return try {
            val numberPart = filename.substringAfter("segment_").substringBefore(".")
            numberPart.toInt()
        } catch (e: Exception) {
            0
        }
    }

    private fun calculateBitrate(width: Int, height: Int): Int {
        return when {
            height <= 240 -> 400_000
            height <= 360 -> 800_000
            height <= 480 -> 1_200_000
            height <= 720 -> 2_500_000
            else -> 4_000_000
        }
    }

    fun cleanupOldSegments(streamKey: String, keepLastCount: Int = 20) {
        SUPPORTED_RESOLUTIONS.keys.forEach { resolution ->
            try {
                val streamDir = File(System.getProperty("java.io.tmpdir"), "streams/$streamKey/$resolution")
                if (streamDir.exists()) {
                    val segmentFiles = streamDir.listFiles { _, name -> 
                        name.endsWith(".ts") 
                    }?.sortedBy { it.name } ?: return@forEach
                    
                    if (segmentFiles.size > keepLastCount) {
                        segmentFiles.dropLast(keepLastCount).forEach { file ->
                            if (file.delete()) {
                                logger.debug("Deleted old segment: ${file.name}")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                logger.error("Error cleaning up old segments for $streamKey/$resolution", e)
            }
        }
    }
}