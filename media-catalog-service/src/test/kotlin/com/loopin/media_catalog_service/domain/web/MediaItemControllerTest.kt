package com.loopin.media_catalog_service.domain.web

import com.loopin.media_catalog_service.config.SecurityConfig
import com.loopin.media_catalog_service.domain.model.MediaItem
import com.loopin.media_catalog_service.domain.service.MediaItemService
import com.loopin.media_catalog_service.domain.web.dto.IdListDto
import com.loopin.media_catalog_service.domain.web.dto.MediaItemDto
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.time.Instant

/**
 * MediaItemController 통합-슬라이스 테스트
 *
 * - SecurityConfig 를 import 해서 실제 보안 필터 체인을 사용
 * - WebTestClient 로 HTTP 호출 및 응답 검증
 */
@WebFluxTest(controllers = [MediaItemController::class])
@Import(SecurityConfig::class)
@AutoConfigureWebTestClient
class MediaItemControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockitoBean
    lateinit var svc: MediaItemService

    /* ───────────── permitAll: POST /api/v1/items/batch ───────────── */
    @Nested
    @DisplayName("POST /items/batch (permitAll)")
    inner class PostBatch {

        @Test
        fun `id 순서를 보존하여 DTO 리스트 반환`() {
            // given
            val ids = listOf(1L, 2L, 3L)

            val publishedAt = Instant.parse("2025-01-01T00:00:00Z")

            val item1 = MediaItem(
                id = 1L,
                resourceId = "VID_A",
                title = "Video A",
                description = "Desc A",
                kind = "youtube#video",
                publishedAt = publishedAt,
                thumbnail = null,
                videoOwnerChannelId = "CHAN_1",
                videoOwnerChannelTitle = "Channel 1",
                platformType = "YOUTUBE",
                durationSeconds = 180L
            )
            val item2 = item1.copy(id = 2L, resourceId = "VID_B", title = "Video B")
            val item3 = item1.copy(id = 3L, resourceId = "VID_C", title = "Video C")

            whenever(svc.findAllByIdPreserveOrder(ids))
                .thenReturn(Flux.just(item1, item2, item3))

            // when + then
            webTestClient.post()
                .uri("/api/v1/items/batch")
                .bodyValue(IdListDto(ids))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].resourceId").isEqualTo("VID_A")
                .jsonPath("$[1].title").isEqualTo("Video B")
                .jsonPath("$[2].resourceId").isEqualTo("VID_C")

            verify(svc).findAllByIdPreserveOrder(ids)
        }

        @Test
        fun `무토큰 요청도 200`() {
            val ids = listOf(10L)
            whenever(svc.findAllByIdPreserveOrder(ids))
                .thenReturn(Flux.just(
                    MediaItem(
                        id = 10L,
                        resourceId = "XYZ",
                        title = "Sample",
                        description = null,
                        kind = "youtube#video",
                        publishedAt = Instant.now(),
                        thumbnail = null,
                        videoOwnerChannelId = null,
                        videoOwnerChannelTitle = null,
                        platformType = "YOUTUBE",
                        durationSeconds = 60L
                    )
                ))

            webTestClient.post()
                .uri("/api/v1/items/batch")
                .bodyValue(IdListDto(ids))
                .exchange()
                .expectStatus().isOk
        }

        @Test
        fun `JWT 포함 요청도 정상 200`() {
            val ids = listOf(5L)
            whenever(svc.findAllByIdPreserveOrder(ids))
                .thenReturn(Flux.empty())

            val authed = webTestClient.mutateWith(mockJwt())

            authed.post()
                .uri("/api/v1/items/batch")
                .bodyValue(IdListDto(ids))
                .exchange()
                .expectStatus().isOk
                .expectBody().json("[]")
        }

        @Test
        fun `ids 300 초과 시 400`() {
            val tooMany = (1..301L).toList()

            webTestClient.post()
                .uri("/api/v1/items/batch")
                .bodyValue(IdListDto(tooMany))
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.error").isEqualTo("Bad Request")       // 선택: 메시지 검증
                .jsonPath("$.path").isEqualTo("/api/v1/items/batch")

            verify(svc, never()).findAllByIdPreserveOrder(any())
        }
    }
}