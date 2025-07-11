package com.loopin.playback_service.domain.service

import com.loopin.playback_service.domain.model.PlaySession
import com.loopin.playback_service.domain.repository.PlaySessionRepository
import com.loopin.playback_service.domain.web.dto.UserPlaySessionDto
import com.loopin.playback_service.domain.web.mapper.toDto
import com.loopin.playback_service.media_catalog.MediaCatalogClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserPlaySessionService(
    private val mediaCatalogClient: MediaCatalogClient,
    private val playSessionRepository: PlaySessionRepository,
) {

    companion object {
        private const val BATCH_LIMIT = 300
    }

    private val logger = LoggerFactory.getLogger(javaClass)

    /** 300개 단위로 끊어서 병렬-호출 후 합치기 */
    private fun <ID, DTO> fetchInChunks(
        ids: List<ID>,
        fetcher: (List<ID>) -> Mono<out List<DTO>>,
    ): Mono<List<DTO>> =
        Flux.fromIterable(ids.distinct().chunked(BATCH_LIMIT))   // 중복 제거 + chunk
            .flatMapSequential(fetcher)                           // 각 chunk 호출
            .collectList()                                        // List<List<DTO>>
            .map { it.flatten() }                                 // 평탄화

    /** 유저의 모든 세션 + 관련 메타 (현재곡만) */
    fun getUserPlaySessions(userId: String): Flux<UserPlaySessionDto> =
        playSessionRepository.findAllByUserId(userId)
            .collectList()
            .flatMapMany { sessions ->
                if (sessions.isEmpty()) return@flatMapMany Flux.empty()

                val playlistIds = sessions.map { it.mediaPlaylistId }
                val itemIds = sessions.map { it.nowPlayingItemId }

                Mono.zip(
                    fetchInChunks(playlistIds, mediaCatalogClient::getMediaPlaylistBatch),
                    fetchInChunks(itemIds, mediaCatalogClient::getMediaItemBatch)
                ) { playlists, items -> playlists to items }
                    .flatMapMany { (playlists, items) ->
                        val playlistMap = playlists.associateBy { it.id }
                        val itemMap = items.associateBy { it.id }

                        Flux.fromIterable(
                            sessions.map { ps ->
                                ps.toDto(
                                    playlist = playlistMap[ps.mediaPlaylistId]
                                        ?: error("playlist ${ps.mediaPlaylistId} not found"),
                                    now = itemMap[ps.nowPlayingItemId]
                                        ?: error("item ${ps.nowPlayingItemId} not found"),
                                    prev = null,
                                    next = null,
                                    prevItemsLength = ps.prevItems.size,
                                    nextItemsLength = ps.nextItems.size,
                                )
                            }
                        )
                    }
            }

    /** 특정 플레이리스트 세션 1건 + 큐 포함 */
    fun getUserPlaySessionByPlaylistIdWithItems(
        userId: String,
        playlistId: Long,
    ): Mono<UserPlaySessionDto> =
        playSessionRepository.findByUserIdAndMediaPlaylistId(userId, playlistId)
            .switchIfEmpty(Mono.error(NoSuchElementException("session not found")))
            .flatMap { ps ->
                val idsToFetch = listOf(ps.nowPlayingItemId) + ps.prevItems + ps.nextItems

                Mono.zip(
                    fetchInChunks(listOf(ps.mediaPlaylistId), mediaCatalogClient::getMediaPlaylistBatch)
                        .map { it.first() },                // 단일 플레이리스트
                    fetchInChunks(idsToFetch, mediaCatalogClient::getMediaItemBatch)
                ) { playlist, items -> playlist to items.associateBy { it.id } }
                    .map { (playlist, itemMap) ->
                        ps.toDto(
                            playlist = playlist,
                            now = itemMap[ps.nowPlayingItemId]
                                ?: error("nowPlaying not found"),
                            prev = ps.prevItems.mapNotNull(itemMap::get),
                            next = ps.nextItems.mapNotNull(itemMap::get),
                            prevItemsLength = ps.prevItems.size,
                            nextItemsLength = ps.nextItems.size,
                        )
                    }
            }

    /** Upsert : 있으면 UPDATE, 없으면 INSERT */
    fun upsertUserPlaySession(playSession: PlaySession): Mono<Void> =
        playSessionRepository                       // ① 조회
            .findByUserIdAndMediaPlaylistId(
                userId = playSession.userId,
                mediaPlaylistId = playSession.mediaPlaylistId
            )
            .flatMap { current ->                   // ② 존재 → copy 후 UPDATE
                val updated = current.copy(
                    nowPlayingItemId = playSession.nowPlayingItemId,
                    startSeconds = playSession.startSeconds,
                    prevItems = playSession.prevItems,
                    nextItems = playSession.nextItems,
                )
                playSessionRepository.save(updated)
            }
            .switchIfEmpty(                         // ③ 없으면 INSERT
                playSessionRepository.save(playSession)
            )
            .then()

    fun updateStartSeconds(userId: String, mediaPlaylistId: Long, startSeconds: Int): Mono<Void> =
        playSessionRepository
            .updateStartSeconds(userId, mediaPlaylistId, startSeconds)
            .flatMap { updatedRows ->
                if (updatedRows == 0) Mono.error(NoSuchElementException("session not found"))
                else Mono.empty()
            }
}