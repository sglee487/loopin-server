package com.loopin.playback_service.domain.web

import com.loopin.playback_service.domain.model.PlaySession
import com.loopin.playback_service.domain.service.UserPlaySessionService
import com.loopin.playback_service.domain.web.dto.PutUserPlaySessionRequestDto
import com.loopin.playback_service.domain.web.dto.UpdateStartSecondsRequestDto
import com.loopin.playback_service.domain.web.dto.UserPlaySessionDto
import com.loopin.playback_service.media_catalog.MediaItem
import com.loopin.playback_service.media_catalog.MediaPlaylist
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@WebFluxTest(controllers = [UserPlaySessionController::class])
@AutoConfigureWebTestClient
class UserPlaySessionControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockitoBean
    lateinit var svc: UserPlaySessionService

    private val authClient by lazy {
        webTestClient.mutateWith(
            mockJwt()
                .jwt { it.subject("test-user").claim("roles", listOf("USER")) }
                .authorities(SimpleGrantedAuthority("ROLE_USER"))
        ).mutateWith(csrf())
    }

    /* ────────────── Dummy domain objects ────────────── */
    private val dummyPlaylist = MediaPlaylist(
        id = 1L,
        resourceId = "PL_xyz123",
        title = "샘플 플레이리스트",
        description = "테스트용 설명",
        kind = "youtube#playlist",
        thumbnail = "https://example.com/playlist-thumb.jpg",
        channelId = "UC_ABCDEF",
        channelTitle = "샘플 채널",
        publishedAt = Instant.parse("2024-01-01T00:00:00Z"),
        platformType = "YOUTUBE",
        itemCount = 20,
        createdAt = Instant.parse("2024-01-02T00:00:00Z"),
        updatedAt = Instant.parse("2024-01-03T00:00:00Z"),
        createdBy = "init",
        updatedBy = "init",
    )

    private val dummyItem = MediaItem(
        id = 10L,
        resourceId = "VID_123456",
        title = "샘플 비디오",
        description = "테스트용 비디오 설명",
        kind = "youtube#video",
        publishedAt = Instant.parse("2024-02-01T00:00:00Z"),
        thumbnail = "https://example.com/video-thumb.jpg",
        videoOwnerChannelId = "UC_ABCDEF",
        videoOwnerChannelTitle = "샘플 채널",
        platformType = "YOUTUBE",
        durationSeconds = 300,
        createdAt = Instant.parse("2024-02-01T00:00:00Z"),
        updatedAt = Instant.parse("2024-02-01T00:00:00Z"),
        createdBy = "init",
        updatedBy = "init",
    )

    private val sampleDto = UserPlaySessionDto(
        id = 100L,
        userId = "test-user",
        startSeconds = 5,
        playlist = dummyPlaylist,
        nowPlaying = dummyItem,
        prevItems = listOf(dummyItem),
        nextItems = listOf(dummyItem),
        prevItemsLength = 1,
        nextItemsLength = 1,
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )

    /* ───────────── GET /sessions ───────────── */
    @Nested
    @DisplayName("GET /sessions")
    inner class ListSessions {
        @Test
        fun `현재 사용자 세션 목록 조회`() {
            whenever(svc.getUserPlaySessions("test-user"))
                .thenReturn(Flux.just(sampleDto))

            authClient
                .get().uri("/api/v1/user-play-session/sessions")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(UserPlaySessionDto::class.java).hasSize(1)

            verify(svc, times(1)).getUserPlaySessions("test-user")
        }
    }

    /* ───────────── GET /sessions/{playlistId} ───────────── */
    @Nested
    @DisplayName("GET /sessions/{playlistId}")
    inner class GetSession {
        @Test
        fun `플레이리스트 단건 세션+큐 조회`() {
            whenever(
                svc.getUserPlaySessionByPlaylistIdWithItems("test-user", 1L)
            ).thenReturn(Mono.just(sampleDto))

            authClient
                .get().uri("/api/v1/user-play-session/sessions/{id}", 1)
                .exchange()
                .expectStatus().isOk
                .expectBody().jsonPath("$.id").isEqualTo(sampleDto.id)

            verify(svc).getUserPlaySessionByPlaylistIdWithItems("test-user", 1L)
        }
    }

    /* ───────────── PUT /sessions/{playlistId} ───────────── */
    @Nested
    @DisplayName("PUT /sessions/{playlistId}")
    inner class PutSession {

        @Test
        fun `세션 upsert`() {
            val body = PutUserPlaySessionRequestDto(
                nowPlayingItemId = 10L,
                startSeconds = 0,
                prevItems = listOf(1L, 2L),
                nextItems = listOf(3L, 4L),
            )

            whenever(svc.upsertUserPlaySession(any())).thenReturn(Mono.empty())

            authClient
                .put().uri("/api/v1/user-play-session/sessions/{id}", 1)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk
                .expectBody().isEmpty

            /* ✅ captor 타입을 생략하거나 KArgumentCaptor 로 선언 */
            val captor = argumentCaptor<PlaySession>()   // ← 여기만 변경

            verify(svc).upsertUserPlaySession(captor.capture())

            val saved = captor.firstValue
            // 필요 시 saved 필드 검증
            Assertions.assertEquals("test-user", saved.userId)
        }
    }

    /* ───────────── PATCH /sessions/{playlistId}/start-seconds ───────────── */
    @Nested
    @DisplayName("PATCH /sessions/{playlistId}/start-seconds")
    inner class PatchStartSeconds {
        @Test
        fun `startSeconds 부분 업데이트`() {
            whenever(svc.updateStartSeconds("test-user", 1L, 120))
                .thenReturn(Mono.empty())

            authClient
                .patch().uri("/api/v1/user-play-session/sessions/{id}/start-seconds", 1)
                .bodyValue(UpdateStartSecondsRequestDto(startSeconds = 120))
                .exchange()
                .expectStatus().isOk

            verify(svc).updateStartSeconds("test-user", 1L, 120)
        }
    }
}
