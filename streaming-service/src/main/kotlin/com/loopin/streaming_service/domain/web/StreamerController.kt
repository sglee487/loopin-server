package com.loopin.streaming_service.domain.web

import com.loopin.streaming_service.domain.service.StreamService
import com.loopin.streaming_service.domain.web.dto.CreateStreamRequestDto
import com.loopin.streaming_service.domain.web.dto.StreamerResponseDto
import com.loopin.streaming_service.domain.web.dto.UpdateStreamRequestDto
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * StreamerController - 스트리머 전용 스트림 키 관리 API
 *
 * 역할:
 * - 스트림 키 생성/갱신 관리 (실제 스트림은 SRS 서버에서 생성)
 * - 자신의 스트림 키 조회 (유저당 하나)
 * - streamKey 정보 포함 (스트리머만 알 수 있음)
 */
@RestController
@RequestMapping("/api/v1/stream")
class StreamerController(
    private val streamService: StreamService
) {

    @PostMapping
    fun createStreamKey(
        @RequestBody request: CreateStreamRequestDto,
        principal: JwtAuthenticationToken,
    ): Mono<StreamerResponseDto> {
        return streamService.createStreamKey(
            request = request,
            userId = principal.name,
        )
    }

    @GetMapping
    fun getMyStreamKey(
        principal: JwtAuthenticationToken
    ): Mono<StreamerResponseDto> {
        return streamService.getMyStream(principal.name)
    }

    @PatchMapping
    fun updateStreamMetadata(
        @RequestBody request: UpdateStreamRequestDto,
        principal: JwtAuthenticationToken
    ): Mono<StreamerResponseDto> {
        return streamService.updateStreamMetadata(principal.name, request)
    }

    @PatchMapping("/refresh")
    fun refreshStreamKey(
        principal: JwtAuthenticationToken
    ): Mono<StreamerResponseDto> {
        return streamService.refreshStreamKey(principal.name)
    }

}