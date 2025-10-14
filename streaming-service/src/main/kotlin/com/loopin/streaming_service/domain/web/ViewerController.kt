package com.loopin.streaming_service.domain.web

import com.loopin.streaming_service.domain.service.StreamService
import com.loopin.streaming_service.domain.web.dto.ViewerResponseDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * ViewerController - 시청자 전용 스트림 시청 API
 *
 * 역할:
 * - 스트림 목록 조회 (공개 스트림만)
 * - HLS 플레이리스트 및 세그먼트 제공
 * - streamKey 정보 숨김 (보안)
 * - publicId 기반 스트림 식별
 */
@RestController
@RequestMapping("/api/v1/streams")
class ViewerController(
    @Value("\${streaming.base-url}") private val baseURL: String,
    private val streamService: StreamService
) {

    private val logger = LoggerFactory.getLogger(ViewerController::class.java)

    @GetMapping("/{publicId}")
    fun getStream(@PathVariable publicId: String): Mono<ViewerResponseDto> {
        return streamService.getStreamByPublicId(publicId)
    }

    @GetMapping
    fun getLiveStreams(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): Flux<ViewerResponseDto> {
        return streamService.getLiveStreams(page, size)
    }

    @GetMapping("/hls/{publicId}/master.m3u8")
    fun getMasterPlaylist(@PathVariable publicId: String): Mono<ResponseEntity<String>> {
        logger.info("HLS master.m3u8 request: publicId=$publicId")

        val masterPlaylist = """
            #EXTM3U
            #EXT-X-VERSION:3
            #EXT-X-INDEPENDENT-SEGMENTS

            # 720p (30fps) - 고화질
            #EXT-X-STREAM-INF:BANDWIDTH=1750000,RESOLUTION=1280x720,FRAME-RATE=30
            ${baseURL}/api/v1/streams/hls/${publicId}/seg/720p/index.m3u8

            # 360p (30fps) - 저화질
            #EXT-X-STREAM-INF:BANDWIDTH=300000,RESOLUTION=640x360,FRAME-RATE=30
            ${baseURL}/api/v1/streams/hls/${publicId}/seg/360p/index.m3u8

        """.trimIndent()

        return Mono.just(
            ResponseEntity.ok()
                .header("Content-Type", "application/vnd.apple.mpegurl")
                .body(masterPlaylist)
        )
    }

    @GetMapping("/hls/{slug}/seg/{variant}/{file}")
    fun seg(@PathVariable slug: String, @PathVariable variant: String, @PathVariable file: String)
            : Mono<ResponseEntity<Void>> {

        logger.info("HLS seg request: slug=$slug, variant=$variant, file=$file")

        return mapSlugToSecret(slug).map { secret ->
            logger.info("HLS seg request: secret=$secret")
            val internalPath = "/_internal/hls/abr/live/${secret}_${variant}/$file"
            logger.info("HLS seg request: internalPath=$internalPath")

            // m3u8 파일은 캐싱하지 않고, ts 파일은 캐싱 허용
            val responseBuilder = ResponseEntity.ok()
                .header("X-Accel-Redirect", internalPath)

            if (file.endsWith(".m3u8")) {
                responseBuilder
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .header("Expires", "0")
            }
            responseBuilder.build()
        }
    }

    private fun mapSlugToSecret(slug: String): Mono<String> {
        return streamService.getStreamKeyByPublicId(slug)
    }
}