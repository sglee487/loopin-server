package com.loopin.playback_service.domain.service

import com.loopin.playback_service.domain.model.PlaySession
import com.loopin.playback_service.domain.repository.PlaySessionRepository
import com.loopin.playback_service.domain.web.dto.UserPlaySessionDto
import com.loopin.playback_service.media_catalog.MediaCatalogClient
import com.loopin.playback_service.media_catalog.MediaItem
import com.loopin.playback_service.media_catalog.MediaPlaylist
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Instant

class UserPlaySessionServiceTest {

    /** ────── Mocks & SUT ────── */
    private val catalog: MediaCatalogClient = mock()
    private val repo: PlaySessionRepository = mock()
    private val svc = UserPlaySessionService(catalog, repo)

    /* ───────────── 더미 데이터 ───────────── */
    private val playlist = MediaPlaylist(
        id = 1L, resourceId = "PL_x", title = "P", description = null,
        kind = "youtube#playlist", thumbnail = null, channelId = "CH",
        channelTitle = "CH", publishedAt = Instant.now(), platformType = "YOUTUBE",
        itemCount = 1
    )
    private val item = MediaItem(
        id = 10L, resourceId = "VID_x", title = "V", description = null,
        kind = "youtube#video", publishedAt = Instant.now(), thumbnail = null,
        videoOwnerChannelId = null, videoOwnerChannelTitle = null,
        platformType = "YOUTUBE", durationSeconds = 100
    )
    private val session = PlaySession(
        id = 100L, userId = "u1", mediaPlaylistId = 1L, nowPlayingItemId = 10L
    )

    /* ───────────── getUserPlaySessions ───────────── */
    @Nested
    @DisplayName("getUserPlaySessions")
    inner class List {

        @Test
        fun `세션 없으면 empty`() {
            whenever(repo.findAllByUserId("u1")).thenReturn(Flux.empty())

            StepVerifier.create(svc.getUserPlaySessions("u1"))
                .verifyComplete()
        }

        @Test
        fun `세션 + 메타 정상 매핑`() {
            whenever(repo.findAllByUserId("u1")).thenReturn(Flux.just(session))
            whenever(catalog.getMediaPlaylistBatch(listOf(1L))).thenReturn(Mono.just(listOf(playlist)))
            whenever(catalog.getMediaItemBatch(listOf(10L))).thenReturn(Mono.just(listOf(item)))

            StepVerifier.create(svc.getUserPlaySessions("u1"))
                .assertNext { dto ->
                    assert(dto is UserPlaySessionDto)
                    assert(dto.playlist.id == 1L)
                    assert(dto.nowPlaying.id == 10L)
                }
                .verifyComplete()

            verify(repo).findAllByUserId("u1")
            verify(catalog).getMediaPlaylistBatch(listOf(1L))
            verify(catalog).getMediaItemBatch(listOf(10L))
        }
    }

    /* ───────────── getUserPlaySessionByPlaylistIdWithItems ───────────── */
    @Nested
    @DisplayName("getUserPlaySessionByPlaylistIdWithItems")
    inner class Detail {

        @Test
        fun `존재하지 않으면 에러`() {
            whenever(repo.findByUserIdAndMediaPlaylistId("u1", 1L)).thenReturn(Mono.empty())

            StepVerifier.create(svc.getUserPlaySessionByPlaylistIdWithItems("u1", 1L))
                .expectError(NoSuchElementException::class.java)
                .verify()
        }

        @Test
        fun `모든 아이템 매핑`() {
            val ps = session.copy(prevItems = listOf(), nextItems = listOf())
            whenever(repo.findByUserIdAndMediaPlaylistId("u1", 1L)).thenReturn(Mono.just(ps))
            whenever(catalog.getMediaPlaylistBatch(listOf(1L))).thenReturn(Mono.just(listOf(playlist)))
            whenever(catalog.getMediaItemBatch(listOf(10L))).thenReturn(Mono.just(listOf(item)))

            StepVerifier.create(svc.getUserPlaySessionByPlaylistIdWithItems("u1", 1L))
                .assertNext { dto ->
                    assert(dto.nowPlaying.id == 10L)
                    assert(dto.playlist.id == 1L)
                }
                .verifyComplete()
        }
    }

    /* ───────────── upsertUserPlaySession ───────────── */
    @Nested
    @DisplayName("upsertUserPlaySession")
    inner class Upsert {

        @Test
        fun `존재 → UPDATE 경로`() {
            whenever(repo.findByUserIdAndMediaPlaylistId("u1", 1L))
                .thenReturn(Mono.just(session))
            whenever(repo.save(any())).thenReturn(Mono.just(session))

            StepVerifier.create(svc.upsertUserPlaySession(session))
                .verifyComplete()

            /* ✅ 최소 1회로 변경 + 값 검증 */
            val captor = argumentCaptor<PlaySession>()
            verify(repo, atLeastOnce()).save(captor.capture())

            // 첫 번째 save 에 들어간 객체 확인
            assert(captor.firstValue.nowPlayingItemId == 10L)
        }
    }

    /* ───────────── updateStartSeconds ───────────── */
    @Nested
    @DisplayName("updateStartSeconds")
    inner class PatchSec {

        @Test
        fun `영향행 0 → 에러`() {
            whenever(repo.updateStartSeconds("u1", 1L, 5)).thenReturn(Mono.just(0))

            StepVerifier.create(svc.updateStartSeconds("u1", 1L, 5))
                .expectError(NoSuchElementException::class.java)
                .verify()
        }

        @Test
        fun `성공시 void 완료`() {
            whenever(repo.updateStartSeconds("u1", 1L, 5)).thenReturn(Mono.just(1))

            StepVerifier.create(svc.updateStartSeconds("u1", 1L, 5))
                .verifyComplete()
        }
    }
}
