package com.loopin.streaming_service.domain.web

import com.loopin.streaming_service.domain.service.StreamService
import com.loopin.streaming_service.domain.web.dto.CreateStreamRequestDto
import com.loopin.streaming_service.domain.web.dto.StreamResponseDto
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/streams")
class StreamController(
    private val streamService: StreamService
) {

    @PostMapping
    fun createStream(@RequestBody request: CreateStreamRequestDto): Mono<StreamResponseDto> {
        return streamService.createStream(request)
    }

    @GetMapping("/{streamKey}")
    fun getStream(@PathVariable streamKey: String): Mono<StreamResponseDto> {
        return streamService.getStreamByKey(streamKey)
    }

    @GetMapping
    fun getStreams(@RequestParam(defaultValue = "0") page: Int,
                   @RequestParam(defaultValue = "20") size: Int): Flux<StreamResponseDto> {
        return streamService.getLiveStreams(page, size)
    }

    @PostMapping("/{streamKey}/start")
    fun startStream(@PathVariable streamKey: String): Mono<StreamResponseDto> {
        return streamService.startStream(streamKey)
    }

    @PostMapping("/{streamKey}/stop")
    fun stopStream(@PathVariable streamKey: String): Mono<StreamResponseDto> {
        return streamService.stopStream(streamKey)
    }

    @GetMapping("/{streamKey}/playlist.m3u8", produces = ["application/x-mpegURL"])
    fun getMasterPlaylist(@PathVariable streamKey: String): Mono<ResponseEntity<String>> {
        return streamService.getMasterPlaylist(streamKey)
            .map { playlist ->
                ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/x-mpegURL"))
                    .header("Cache-Control", "no-cache")
                    .body(playlist)
            }
    }

    @GetMapping("/{streamKey}/{resolution}/playlist.m3u8", produces = ["application/x-mpegURL"])
    fun getResolutionPlaylist(
        @PathVariable streamKey: String,
        @PathVariable resolution: String
    ): Mono<ResponseEntity<String>> {
        return streamService.getResolutionPlaylist(streamKey, resolution)
            .map { playlist ->
                ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/x-mpegURL"))
                    .header("Cache-Control", "no-cache")
                    .body(playlist)
            }
    }

    @GetMapping("/{streamKey}/{resolution}/{segmentName}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getSegment(
        @PathVariable streamKey: String,
        @PathVariable resolution: String,
        @PathVariable segmentName: String
    ): Mono<ResponseEntity<Resource>> {
        return streamService.getSegment(streamKey, resolution, segmentName)
            .map { resource ->
                ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/mp2t"))
                    .header("Cache-Control", "public, max-age=31536000")
                    .body(resource)
            }
    }
}