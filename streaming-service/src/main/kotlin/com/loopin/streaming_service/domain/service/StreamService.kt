package com.loopin.streaming_service.domain.service

import com.loopin.streaming_service.domain.model.StreamSession
import com.loopin.streaming_service.domain.model.StreamStatus
import com.loopin.streaming_service.domain.repository.StreamSessionRepository
import com.loopin.streaming_service.domain.web.dto.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class StreamService(
    private val streamSessionRepository: StreamSessionRepository,
    @Value("\${streaming.rtmp-url}") private val rtmpBaseUrl: String
) {

    private val logger = LoggerFactory.getLogger(StreamService::class.java)

    fun createStreamKey(request: CreateStreamRequestDto, userId: String): Mono<StreamerResponseDto> {
        return streamSessionRepository.findByStreamerId(userId)
            .flatMap<StreamerResponseDto> {
                Mono.error(IllegalStateException("User already has a stream key. Use PATCH to update metadata or PATCH /refresh to get new key."))
            }
            .switchIfEmpty(
                // 새 스트림 생성
                Mono.fromCallable {
                    StreamSession.create(
                        title = request.title,
                        description = request.description,
                        streamerId = userId,
                        resolution = request.resolution,
                        bitrate = request.bitrate
                    )
                }.flatMap(streamSessionRepository::save)
                    .map(::toStreamerDto)
            )
            .doOnSuccess { logger.info("Stream key created for user: $userId") }
    }

    fun getStreamByKey(streamKey: String): Mono<StreamResponseDto> {
        return streamSessionRepository.findByStreamKey(streamKey)
            .map(::toResponseDto)
    }

    fun getStreamByPublicId(publicId: String): Mono<ViewerResponseDto> {
        return streamSessionRepository.findByPublicId(publicId)
            .map(::toViewerDto)
    }

    fun getStreamKeyByPublicId(publicId: String): Mono<String> {
        return streamSessionRepository.findByPublicId(publicId)
            .map { it.streamKey }
    }

    fun getLiveStreams(page: Int, size: Int): Flux<ViewerResponseDto> {
        return streamSessionRepository.findByStatus(StreamStatus.LIVE)
            .skip((page * size).toLong())
            .take(size.toLong())
            .map(::toViewerDto)
    }

    fun getMyStream(streamerId: String): Mono<StreamerResponseDto> {
        return streamSessionRepository.findByStreamerId(streamerId)
            .map(::toStreamerDto)
    }

    fun refreshStreamKey(streamerId: String): Mono<StreamerResponseDto> {
        return streamSessionRepository.findByStreamerId(streamerId)
            .flatMap { existingStream ->
                val refreshedStream = existingStream.refreshStreamKey()
                streamSessionRepository.save(refreshedStream)
            }
            .map(::toStreamerDto)
            .doOnSuccess { logger.info("Stream key refreshed for user: $streamerId") }
    }

    fun updateStreamMetadata(streamerId: String, request: UpdateStreamRequestDto): Mono<StreamerResponseDto> {
        return streamSessionRepository.findByStreamerId(streamerId)
            .flatMap { existingStream ->
                val updatedStream = existingStream.copy(
                    title = request.title ?: existingStream.title,
                    description = request.description ?: existingStream.description,
                    resolution = request.resolution ?: existingStream.resolution,
                    bitrate = request.bitrate ?: existingStream.bitrate
                )
                streamSessionRepository.save(updatedStream)
            }
            .map(::toStreamerDto)
            .doOnSuccess { logger.info("Stream metadata updated for user: $streamerId") }
    }

    fun startStream(streamKey: String): Mono<StreamResponseDto> {
        return streamSessionRepository.findByStreamKey(streamKey)
            .flatMap { stream ->
                val updatedStream = stream.copy(status = StreamStatus.LIVE)
                streamSessionRepository.save(updatedStream)
            }
            .map(::toResponseDto)
    }

    fun stopStream(streamKey: String): Mono<StreamResponseDto> {
        return streamSessionRepository.findByStreamKey(streamKey)
            .flatMap { stream ->
                val updatedStream = stream.copy(status = StreamStatus.ENDED)
                streamSessionRepository.save(updatedStream)
            }
            .map(::toResponseDto)
    }

    private fun toResponseDto(
        stream: StreamSession,
        includeStreamKey: Boolean = true,
        includeStreamerId: Boolean = true
    ): StreamResponseDto {
        return StreamResponseDto(
            id = stream.id!!,
            publicId = stream.publicId,
            streamKey = if (includeStreamKey) stream.streamKey else null,
            title = stream.title,
            description = stream.description,
            status = stream.status,
            streamerId = if (includeStreamerId) stream.streamerId else null,
            resolution = stream.resolution,
            bitrate = stream.bitrate,
            viewersCount = stream.viewersCount,
            createdAt = stream.createdAt!!,
            updatedAt = stream.updatedAt,
            hlsUrl = "/api/v1/streams/${stream.publicId}/playlist.m3u8"
        )
    }

    private fun toStreamerDto(stream: StreamSession): StreamerResponseDto {
        return StreamerResponseDto(
            id = stream.id!!,
            publicId = stream.publicId,
            streamKey = stream.streamKey,
            rtmpUrl = rtmpBaseUrl,  // OBS Server 필드용 (예: rtmp://host:1935/live)
            title = stream.title,
            description = stream.description,
            status = stream.status,
            streamerId = stream.streamerId,
            resolution = stream.resolution,
            bitrate = stream.bitrate,
            viewersCount = stream.viewersCount,
            createdAt = stream.createdAt!!,
            updatedAt = stream.updatedAt,
            hlsUrl = "/api/v1/streams/${stream.publicId}/playlist.m3u8"
        )
    }

    private fun toViewerDto(stream: StreamSession): ViewerResponseDto {
        return ViewerResponseDto(
            id = stream.id!!,
            publicId = stream.publicId,
            title = stream.title,
            description = stream.description,
            status = stream.status,
            resolution = stream.resolution,
            bitrate = stream.bitrate,
            viewersCount = stream.viewersCount,
            createdAt = stream.createdAt!!,
            updatedAt = stream.updatedAt,
            hlsUrl = "/api/v1/streams/${stream.publicId}/playlist.m3u8"
        )
    }
}