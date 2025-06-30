package com.loopin.playback_service.domain.service

import com.loopin.playback_service.domain.model.PlaySession
import com.loopin.playback_service.domain.repository.PlaySessionRepository
import com.loopin.playback_service.domain.web.dto.UserPlaySessionDto
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

    private val logger = LoggerFactory.getLogger(javaClass)

    /** 유저의 모든 세션 + 관련 메타 / 현재곡만 매핑 */
    fun getUserPlaySessions(userId: String): Flux<UserPlaySessionDto> =
        playSessionRepository.findAllByUserId(userId)
            .collectList()
            .flatMapMany { sessions ->
                logger.info("sessions: ${sessions.size}")
                if (sessions.isEmpty()) return@flatMapMany Flux.empty()

                // 1) 필요한 ID 집계
                val playlistIds = sessions.map { it.mediaPlaylistId }.distinct()
                val itemIds = sessions.flatMap {
                    listOf(it.nowPlayingItemId) + it.prevItems + it.nextItems
                }.distinct()

                // 2) 외부 서비스 호출(batch)
                Mono.zip(
                    mediaCatalogClient.getMediaPlaylistBatch(playlistIds),
                    mediaCatalogClient.getMediaItemBatch(itemIds)
                ) { playlists, items -> Pair(playlists, items) }
                    .flatMapMany { (playlists, items) ->
                        logger.info("playlists: ${playlists.size}, items: ${items.size}")
                        TODO()
//                        val playlistMap = playlists.associateBy { it.id }
//                        val itemMap     = items.associateBy { it.id }
//
//                        val dtoList = sessions.map { ps ->
//                            ps.toDto(
//                                playlist = playlistMap[ps.mediaPlaylistId]
//                                    ?: error("playlist ${ps.mediaPlaylistId} not found"),
//                                now      = itemMap[ps.nowPlayingItemId]
//                                    ?: error("item ${ps.nowPlayingItemId} not found"),
//                                prev     = ps.prevItems.mapNotNull(itemMap::get),
//                                next     = ps.nextItems.mapNotNull(itemMap::get)
//                            )
//                        }
//                        Flux.fromIterable(dtoList)
                    }
            }

    /** 특정 플레이리스트 세션 1건 + 큐 포함 반환 */
    fun getUserPlaySessionByPlaylistIdWithItems(
        userId: String,
        playlistId: Long
    ): Mono<UserPlaySessionDto> =
        playSessionRepository.findByUserIdAndMediaPlaylistId(userId, playlistId)
            .switchIfEmpty(Mono.error(NoSuchElementException("session not found")))
            .flatMap { ps ->
                val idsToFetch = listOf(ps.nowPlayingItemId) +
                        ps.prevItems + ps.nextItems

                logger.info(ps.toString())

                TODO()

//                Mono.zip(
//                    mediaCatalogClient.getMediaPlaylistBatch(listOf(ps.mediaPlaylistId))
//                        .map { it.first() },
//                    mediaCatalogClient.getMediaItemBatch(idsToFetch)
//                ) { playlist, items -> Pair(playlist, items.associateBy { it.id }) }
//                    .map { (playlist, itemMap) ->
//                        ps.toDto(
//                            playlist = playlist,
//                            now      = itemMap[ps.nowPlayingItemId]
//                                ?: error("nowPlaying not found"),
//                            prev     = ps.prevItems.mapNotNull(itemMap::get),
//                            next     = ps.nextItems.mapNotNull(itemMap::get)
//                        )
//                    }
            }

    fun createUserPlaySessionByPlaylistId(playSession: PlaySession): Mono<Void> = playSessionRepository.save(
        playSession
    ).then()

    fun updateUserPlaySessionByPlaylistId(playSession: PlaySession): Mono<Void> =
        playSessionRepository
            .findByUserIdAndMediaPlaylistId(playSession.userId, playSession.mediaPlaylistId)
            .switchIfEmpty(Mono.error(NoSuchElementException("session not found")))
            .flatMap { current ->
                val updated = current.copy(
                    nowPlayingItemId = playSession.nowPlayingItemId,
                    startSeconds = playSession.startSeconds,
                    prevItems = playSession.prevItems,
                    nextItems = playSession.nextItems,
                )
                playSessionRepository.save(updated)
            }
            .then()
}