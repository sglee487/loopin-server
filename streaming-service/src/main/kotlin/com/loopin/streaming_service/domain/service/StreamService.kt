package com.loopin.streaming_service.domain.service

import com.loopin.streaming_service.domain.model.StreamSession
import com.loopin.streaming_service.domain.model.StreamStatus
import com.loopin.streaming_service.domain.repository.StreamSessionRepository
import com.loopin.streaming_service.domain.web.dto.CreateStreamRequestDto
import com.loopin.streaming_service.domain.web.dto.StreamResponseDto
import org.springframework.core.io.Resource
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class StreamService(
    private val streamSessionRepository: StreamSessionRepository,
    private val videoConversionService: VideoConversionService,
    private val hlsService: HlsService
) {

    fun createStream(request: CreateStreamRequestDto): Mono<StreamResponseDto> {
        return Mono.fromCallable {
            StreamSession.create(
                title = request.title,
                description = request.description,
                streamerId = "current-user", // TODO: Get from security context
                resolution = request.resolution,
                bitrate = request.bitrate
            )
        }
        .flatMap(streamSessionRepository::save)
        .map(::toResponseDto)
    }

    fun getStreamByKey(streamKey: String): Mono<StreamResponseDto> {
        return streamSessionRepository.findByStreamKey(streamKey)
            .map(::toResponseDto)
    }

    fun getLiveStreams(page: Int, size: Int): Flux<StreamResponseDto> {
        return streamSessionRepository.findByStatus(StreamStatus.LIVE)
            .skip((page * size).toLong())
            .take(size.toLong())
            .map(::toResponseDto)
    }

    fun startStream(streamKey: String): Mono<StreamResponseDto> {
        return streamSessionRepository.findByStreamKey(streamKey)
            .flatMap { stream ->
                val updatedStream = stream.copy(status = StreamStatus.LIVE)
                streamSessionRepository.save(updatedStream)
            }
            .doOnNext { stream ->
                // Start video processing pipeline
                videoConversionService.startStreamProcessing(stream.streamKey)
            }
            .map(::toResponseDto)
    }

    fun stopStream(streamKey: String): Mono<StreamResponseDto> {
        return streamSessionRepository.findByStreamKey(streamKey)
            .flatMap { stream ->
                val updatedStream = stream.copy(status = StreamStatus.ENDED)
                streamSessionRepository.save(updatedStream)
            }
            .doOnNext { stream ->
                // Stop video processing pipeline
                videoConversionService.stopStreamProcessing(stream.streamKey)
            }
            .map(::toResponseDto)
    }

    fun getMasterPlaylist(streamKey: String): Mono<String> {
        return streamSessionRepository.findByStreamKey(streamKey)
            .flatMap { stream ->
                hlsService.generateMasterPlaylist(stream.streamKey, stream.resolution)
            }
    }

    fun getResolutionPlaylist(streamKey: String, resolution: String): Mono<String> {
        return streamSessionRepository.findByStreamKey(streamKey)
            .flatMap { stream ->
                hlsService.generateResolutionPlaylist(stream.streamKey, resolution)
            }
    }

    fun getSegment(streamKey: String, resolution: String, segmentName: String): Mono<Resource> {
        return hlsService.getSegmentFile(streamKey, resolution, segmentName)
    }

    private fun toResponseDto(stream: StreamSession): StreamResponseDto {
        return StreamResponseDto(
            id = stream.id!!,
            streamKey = stream.streamKey,
            title = stream.title,
            description = stream.description,
            status = stream.status,
            streamerId = stream.streamerId,
            resolution = stream.resolution,
            bitrate = stream.bitrate,
            viewersCount = stream.viewersCount,
            createdAt = stream.createdAt!!,
            updatedAt = stream.updatedAt,
            hlsUrl = "/api/v1/streams/${stream.streamKey}/playlist.m3u8"
        )
    }
}