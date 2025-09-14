package com.loopin.streaming_service.infrastructure.srs

import com.loopin.streaming_service.domain.service.StreamService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.io.IOException

@RestController
@RequestMapping("/api/v1/hooks")
class SrsHookController(
    private val streamService: StreamService,
) {

    private val logger = LoggerFactory.getLogger(SrsHookController::class.java)

    @PostMapping("/on_publish")
    fun onPublish(@RequestBody request: SrsWebhookDto): Mono<Map<String, Any>> {
        val streamKey = request.stream

        if (streamKey == null) {
            logger.warn("Stream key is null from IP: ${request.ip}")
            return Mono.just(mapOf("code" to 1, "message" to "Stream key required"))
        }

        // Extract base stream key (remove resolution suffix like _360p, _720p)
        val baseStreamKey = streamKey.substringBefore("_")

        logger.info("Stream publish attempt: $streamKey (base: $baseStreamKey) on app: ${request.app} from IP: ${request.ip}")

        return streamService.getStreamByKey(baseStreamKey)
            .flatMap { stream ->
                logger.info("Stream publish authorized: $streamKey (base: $baseStreamKey)")

                // Generate master.m3u8 using public ID
                generateMasterPlaylist(stream.publicId, baseStreamKey)

                streamService.startStream(baseStreamKey)
                    .map { mapOf<String, Any>("code" to 0) }
            }
            .switchIfEmpty(
                Mono.fromCallable {
                    logger.warn("Invalid or unauthorized stream key attempted: $streamKey (base: $baseStreamKey) from IP: ${request.ip}")
                    mapOf<String, Any>("code" to 1, "message" to "Invalid stream key")
                }
            )
            .onErrorResume { error ->
                logger.error("Error processing publish webhook for $streamKey", error)
                Mono.just(mapOf("code" to 1, "message" to "Stream validation failed"))
            }
    }

    @PostMapping("/on_unpublish")
    fun onUnpublish(@RequestBody request: SrsWebhookDto): Mono<Map<String, Any>> {
        val streamKey = request.stream

        if (streamKey == null) {
            return Mono.just(mapOf("code" to 0))
        }

        // Extract base stream key (remove resolution suffix like _360p, _720p)
        val baseStreamKey = streamKey.substringBefore("_")

        logger.info("Stream unpublish: $streamKey (base: $baseStreamKey) on app: ${request.app}")

        return streamService.stopStream(baseStreamKey)
            .map { mapOf<String, Any>("code" to 0) }
            .onErrorResume { error ->
                logger.error("Error processing unpublish webhook for $streamKey", error)
                Mono.just(mapOf("code" to 0))
            }
    }

    private fun generateMasterPlaylist(publicId: String, streamKey: String) {
        try {
            logger.info("Generating master.m3u8 for publicId: $publicId, streamKey: $streamKey")

            val scriptPath = "/usr/local/srs/objs/generate_master.sh"
            val processBuilder = ProcessBuilder(
                "bash", scriptPath,
                "__defaultVhost__",  // vhost
                "live",               // app
                streamKey,            // stream key
                publicId              // public ID (additional parameter)
            )

            processBuilder.start()
            logger.info("Master playlist generation script executed for publicId: $publicId")

        } catch (e: IOException) {
            logger.error("Failed to generate master playlist for publicId: $publicId", e)
        }
    }
}

