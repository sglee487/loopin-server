package com.loopin.streaming_service.domain.service

import com.loopin.streaming_service.domain.model.StreamInfo
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.Frame
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Service
class VideoConversionService(
    private val hlsService: HlsService
) {
    private val logger = LoggerFactory.getLogger(VideoConversionService::class.java)
    private val executor: ExecutorService = Executors.newCachedThreadPool()
    private val activeStreams = ConcurrentHashMap<String, Boolean>()
    
    companion object {
        private const val SEGMENT_DURATION = 6.0 // seconds
        private val SUPPORTED_RESOLUTIONS = mapOf(
            "240p" to Pair(426, 240),
            "360p" to Pair(640, 360),
            "480p" to Pair(854, 480),
            "720p" to Pair(1280, 720)
        )
    }

    fun convertVideoToHLS(inputStream: InputStream, streamKey: String): Mono<String> {
        return Mono.fromCallable {
            val tempFile = createTempFile(inputStream, streamKey)
            val outputDir = createOutputDirectory(streamKey)
            
            processVideoToMultipleResolutions(tempFile.absolutePath, outputDir, streamKey)
            generateMasterPlaylist(outputDir, streamKey)
            
            outputDir.absolutePath
        }.subscribeOn(Schedulers.boundedElastic())
    }

    fun startStreamProcessing(streamKey: String) {
        activeStreams[streamKey] = true
        logger.info("Started stream processing for key: $streamKey")
        
        executor.submit {
            try {
                processLiveStream(streamKey)
            } catch (e: Exception) {
                logger.error("Error processing live stream $streamKey", e)
                activeStreams.remove(streamKey)
            }
        }
    }

    fun stopStreamProcessing(streamKey: String) {
        activeStreams.remove(streamKey)
        logger.info("Stopped stream processing for key: $streamKey")
    }

    private fun processLiveStream(streamKey: String) {
        // This would be connected to actual live stream input (RTMP, WebRTC, etc.)
        // For now, we'll simulate with a placeholder
        val outputDir = createOutputDirectory(streamKey)
        
        while (activeStreams.containsKey(streamKey)) {
            // Process incoming stream segments
            // This is where real-time encoding would happen
            Thread.sleep(1000) // Simulate processing time
        }
    }

    private fun processVideoToMultipleResolutions(inputPath: String, outputDir: File, streamKey: String) {
        SUPPORTED_RESOLUTIONS.entries.parallelStream().forEach { (resolution, dimensions) ->
            try {
                val resolutionDir = File(outputDir, resolution)
                resolutionDir.mkdirs()
                
                processVideoToResolution(inputPath, resolutionDir, dimensions.first, dimensions.second, resolution)
                logger.info("Processed video to $resolution for stream $streamKey")
            } catch (e: Exception) {
                logger.error("Error processing video to $resolution for stream $streamKey", e)
            }
        }
    }

    private fun processVideoToResolution(inputPath: String, outputDir: File, width: Int, height: Int, resolution: String) {
        FFmpegFrameGrabber(inputPath).use { grabber ->
            grabber.start()
            
            val totalDuration = grabber.lengthInTime
            val segmentCount = (totalDuration / (SEGMENT_DURATION * 1_000_000)).toInt() + 1
            
            var segmentNumber = 0
            var currentTime = 0L
            
            while (currentTime < totalDuration) {
                val segmentEndTime = minOf(currentTime + (SEGMENT_DURATION * 1_000_000).toLong(), totalDuration)
                val segmentFile = File(outputDir, "segment_${segmentNumber}.ts")
                
                FFmpegFrameRecorder(segmentFile.absolutePath, width, height).use { recorder ->
                    configureRecorder(recorder, grabber, width, height)
                    recorder.start()
                    
                    grabber.timestamp = currentTime
                    var frame: Frame?
                    
                    while ((grabber.grabFrame().also { frame = it }) != null && grabber.timestamp < segmentEndTime) {
                        recorder.record(frame)
                    }
                    
                    recorder.stop()
                }
                
                currentTime = segmentEndTime
                segmentNumber++
            }
            
            // Generate playlist for this resolution
            generateResolutionPlaylist(outputDir, segmentNumber, resolution)
        }
    }

    private fun configureRecorder(recorder: FFmpegFrameRecorder, grabber: FFmpegFrameGrabber, width: Int, height: Int) {
        recorder.format = "mpegts"
        recorder.videoCodec = avcodec.AV_CODEC_ID_H264
        recorder.pixelFormat = avutil.AV_PIX_FMT_YUV420P
        recorder.frameRate = grabber.frameRate
        recorder.videoBitrate = calculateBitrate(width, height)
        
        if (grabber.audioChannels > 0) {
            recorder.audioChannels = grabber.audioChannels
            recorder.sampleRate = grabber.sampleRate
            recorder.audioCodec = avcodec.AV_CODEC_ID_AAC
            recorder.audioBitrate = 128000
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

    private fun generateResolutionPlaylist(outputDir: File, segmentCount: Int, resolution: String) {
        val playlistFile = File(outputDir, "playlist.m3u8")
        val playlist = buildString {
            appendLine("#EXTM3U")
            appendLine("#EXT-X-VERSION:3")
            appendLine("#EXT-X-TARGETDURATION:${SEGMENT_DURATION.toInt()}")
            appendLine("#EXT-X-MEDIA-SEQUENCE:0")
            
            for (i in 0 until segmentCount) {
                appendLine("#EXTINF:$SEGMENT_DURATION,")
                appendLine("segment_$i.ts")
            }
            
            appendLine("#EXT-X-ENDLIST")
        }
        
        playlistFile.writeText(playlist)
    }

    private fun generateMasterPlaylist(outputDir: File, streamKey: String) {
        val masterPlaylist = File(outputDir, "index.m3u8")
        val playlist = buildString {
            appendLine("#EXTM3U")
            appendLine("#EXT-X-VERSION:3")
            
            SUPPORTED_RESOLUTIONS.forEach { (resolution, dimensions) ->
                val resolutionDir = File(outputDir, resolution)
                if (resolutionDir.exists()) {
                    val bitrate = calculateBitrate(dimensions.first, dimensions.second)
                    appendLine("#EXT-X-STREAM-INF:BANDWIDTH=$bitrate,RESOLUTION=${dimensions.first}x${dimensions.second}")
                    appendLine("$resolution/playlist.m3u8")
                }
            }
        }
        
        masterPlaylist.writeText(playlist)
    }

    private fun createTempFile(inputStream: InputStream, streamKey: String): File {
        val tempDir = Files.createTempDirectory("stream_$streamKey").toFile()
        val tempFile = File(tempDir, "input.mp4")
        
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        
        return tempFile
    }

    private fun createOutputDirectory(streamKey: String): File {
        val outputDir = File(System.getProperty("java.io.tmpdir"), "streams/$streamKey")
        outputDir.mkdirs()
        return outputDir
    }

    fun getStreamInfo(inputPath: String): StreamInfo? {
        return try {
            FFmpegFrameGrabber(inputPath).use { grabber ->
                grabber.start()
                StreamInfo(
                    duration = grabber.lengthInTime / 1_000_000.0,
                    width = grabber.imageWidth,
                    height = grabber.imageHeight,
                    frameRate = grabber.frameRate,
                    bitrate = grabber.videoBitrate
                )
            }
        } catch (e: Exception) {
            logger.error("Error getting stream info for $inputPath", e)
            null
        }
    }
}