package com.loopin.media_catalog_service.domain.service

import com.davidarvelo.fractionalindexing.FractionalIndexing
import com.loopin.media_catalog_service.domain.exception.AlreadyExistsException
import com.loopin.media_catalog_service.domain.exception.NotExistsException
import com.loopin.media_catalog_service.domain.model.MediaPlaylist
import com.loopin.media_catalog_service.domain.model.PlaylistItemMapping
import com.loopin.media_catalog_service.domain.repository.MediaItemRepository
import com.loopin.media_catalog_service.domain.repository.MediaItemWithPositionRepository
import com.loopin.media_catalog_service.domain.repository.MediaPlaylistRepository
import com.loopin.media_catalog_service.domain.repository.PlaylistItemMappingRepository
import com.loopin.media_catalog_service.domain.web.dto.PlaylistResponseDto
import com.loopin.media_catalog_service.domain.web.mapper.toDto
import com.loopin.media_catalog_service.youtube.YoutubeClient
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.util.concurrent.atomic.AtomicReference

@Service
class MediaPlaylistService(
    private val youtubeClient: YoutubeClient,
    private val mediaPlaylistRepository: MediaPlaylistRepository,
    private val mediaItemRepository: MediaItemRepository,
    private val playlistItemMappingRepository: PlaylistItemMappingRepository,
    private val mediaItemWithPositionRepository: MediaItemWithPositionRepository,
) {

    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    fun getByIdWithItems(id: Long): Mono<PlaylistResponseDto> =
        mediaPlaylistRepository.findById(id)
            .switchIfEmpty(Mono.error(IllegalArgumentException("playlist $id not found")))
            .flatMap { playlist ->
                mediaItemWithPositionRepository
                    .findByPlaylistId(playlist.id!!)
                    .map {
                        logger.info("Found ${it.id} ${it.title} ${it.playlistPosition} ${it.playlistRankKey}")
                        it.toDto(position = it.playlistPosition, rankKey = it.playlistRankKey)
                    }
                    .collectList()
                    .map { items -> playlist.toDto(items) }
            }

    fun getByResourceId(resourceId: String): Mono<MediaPlaylist> {
        return mediaPlaylistRepository.findByResourceId(resourceId)
    }

    fun createByResourceId(resourceId: String): Mono<MediaPlaylist> =
        mediaPlaylistRepository.findByResourceId(resourceId)
            .flatMap {
                Mono.error<MediaPlaylist>(AlreadyExistsException("playlist already exists for $resourceId"))
            }
            .switchIfEmpty(
                youtubeClient.getPlaylistWithItems(resourceId)
                    .flatMap { youtubeData ->
//                        logger.info(youtubeData.toString())
                        logger.debug("Saving playlist ${youtubeData.playlist.resourceId} ${youtubeData.playlist.title}")

                        mediaPlaylistRepository.findByResourceId(youtubeData.playlist.resourceId)
                            .switchIfEmpty(mediaPlaylistRepository.save(youtubeData.playlist))
                            .flatMap { playlist ->

                                logger.debug("Saving ${youtubeData.mediaItem.size} items for playlist ${playlist.id} ${playlist.resourceId} ${playlist.title}")

                                playlistItemMappingRepository.findFirstByPlaylistIdOrderByRankKeyAsc(playlist.id!!)
                                    .map { it.rankKey }
                                    .defaultIfEmpty("")
                                    .flatMap { firstKeyRaw ->
                                        val firstKey: String? = firstKeyRaw.ifEmpty { null }

                                        logger.debug("firstKey: $firstKey")

                                        val prevKeyRef = AtomicReference<String?>(null)

                                        Flux.fromIterable(youtubeData.mediaItem)
                                            .index()
                                            .concatMap { indexedItem ->
                                                val (index, item) = indexedItem
                                                mediaItemRepository.findByResourceId(item.resourceId)
                                                    .switchIfEmpty(mediaItemRepository.save(item))
                                                    .flatMap { savedItem ->
                                                        logger.info(
                                                            "Saving playlist item mapping for playlist ${playlist.id} " +
                                                                    "${playlist.resourceId} ${playlist.title} and media item ${savedItem.id} " +
                                                                    "${savedItem.resourceId} ${savedItem.title} "
                                                        )
                                                        playlistItemMappingRepository.findByPlaylistIdAndMediaItemId(
                                                            playlistId = playlist.id,
                                                            mediaItemId = savedItem.id!!
                                                        )
                                                            .switchIfEmpty(
                                                                Mono.defer {
                                                                    /* ❸ Fractional Index 계산 */
                                                                    val newKey = if (index == 0L) {
                                                                        // 맨 앞에 넣는 첫 번째 항목
                                                                        FractionalIndexing.generateFractionalIndexBetween(
                                                                            null, firstKey
                                                                        )
                                                                    } else {
                                                                        FractionalIndexing.generateFractionalIndexBetween(
                                                                            prevKeyRef.get(), firstKey
                                                                        )
                                                                    }
                                                                    prevKeyRef.set(newKey)

                                                                    playlistItemMappingRepository.save(
                                                                        PlaylistItemMapping(
                                                                            playlistId = playlist.id,
                                                                            mediaItemId = savedItem.id,
                                                                            rankKey = newKey
                                                                        )
                                                                    )
                                                                }
                                                            )
                                                    }
                                            }.then(Mono.just(playlist))
                                    }

                            }
                    }
            )

    fun updateByResourceId(resourceId: String): Mono<MediaPlaylist> =
        youtubeClient.getPlaylistWithItems(resourceId)
            .flatMap { youtubeData ->

                /* ───────────────────── 0. 플레이리스트 존재 확인 ───────────────────── */
                mediaPlaylistRepository.findByResourceId(resourceId)
                    .switchIfEmpty(
                        Mono.error(NotExistsException("Playlist with resourceId $resourceId not found"))
                    )
                    .flatMap { current ->

                        logger.debug("Updating playlist ${current.id} ${current.resourceId} ${current.title}")

                        /* ───────────────────── 1. 메타데이터가 바뀌면 갱신 ───────────────────── */
                        val metaChanged = current.title != youtubeData.playlist.title ||
                                current.description != youtubeData.playlist.description ||
                                current.thumbnail != youtubeData.playlist.thumbnail ||
                                current.itemCount != youtubeData.playlist.itemCount

                        val playlistMono =
                            if (metaChanged) {
                                mediaPlaylistRepository.save(
                                    current.copy(
                                        title = youtubeData.playlist.title,
                                        description = youtubeData.playlist.description,
                                        thumbnail = youtubeData.playlist.thumbnail,
                                        itemCount = youtubeData.playlist.itemCount,
                                    )
                                )
                            } else Mono.just(current)

                        /* ───────────────────── 2. 아이템 upsert + 매핑 upsert ───────────────────── */

                        val newItems = youtubeData.mediaItem
                        val newResIdSet = newItems.map { it.resourceId }.toSet()

                        /* 2-A) 가장 앞 rankKey(firstKey) 만 한 번 조회 */
                        val firstKeyMonoRow: Mono<String> =
                            playlistItemMappingRepository
                                .findFirstByPlaylistIdOrderByRankKeyAsc(current.id!!)
                                .map { it.rankKey }                             // Mono<String>
                                .defaultIfEmpty("")

                        val firstKeyMono: Mono<String?> = firstKeyMonoRow.mapNotNull { it.ifEmpty { null } }

                        val upsertItemsMono: Mono<Void> =
                            firstKeyMono.flatMap { firstKey ->

                                val prevKeyRef = AtomicReference<String?>(null)

                                Flux.fromIterable(newItems)
                                    .index()                 // (idx, MediaItem) ← YouTube 순서: 최신 → 오래된
                                    .concatMap { (idx, newItem) ->

                                        /* 2-B) MediaItem upsert */
                                        mediaItemRepository.findByResourceId(newItem.resourceId)
                                            .switchIfEmpty(mediaItemRepository.save(newItem))
                                            .flatMap { saved ->

                                                /* 내용 달라졌으면 업데이트 */
                                                val needUpdate =
                                                    saved.title != newItem.title ||
                                                            saved.description != newItem.description ||
                                                            saved.thumbnail != newItem.thumbnail ||
                                                            saved.durationSeconds != newItem.durationSeconds

                                                val finalItemMono =
                                                    if (needUpdate)
                                                        mediaItemRepository.save(
                                                            saved.copy(
                                                                title = newItem.title,
                                                                description = newItem.description,
                                                                thumbnail = newItem.thumbnail,
                                                                durationSeconds = newItem.durationSeconds,
                                                            )
                                                        )
                                                    else Mono.just(saved)

                                                /* 2-C) PlaylistItemMapping upsert + rankKey 재계산 */
                                                finalItemMono.flatMap { finalItem ->

                                                    val newKey = if (idx == 0L) {
                                                        FractionalIndexing.generateFractionalIndexBetween(
                                                            null,
                                                            firstKey
                                                        )
                                                    } else {
                                                        FractionalIndexing.generateFractionalIndexBetween(
                                                            prevKeyRef.get(),
                                                            null
                                                        )
                                                    }
                                                    prevKeyRef.set(newKey)

                                                    /* ⬇— 새 upsert 메서드 호출 */
                                                    playlistItemMappingRepository
                                                        .upsertRankKey(
                                                            playlistId = current.id,
                                                            mediaItemId = finalItem.id!!,
                                                            rankKey = newKey,
                                                        )
                                                        .then()              // Mono<Void>
                                                }
                                            }
                                    }
                                    .then()   // Flux<Void> → Mono<Void>
                            }

                        /* ───────────────────── 3. 비공개/삭제된 항목 매핑 제거 ───────────────────── */
                        val deleteOldMappingsMono: Mono<Void> =
                            playlistItemMappingRepository.findByPlaylistId(current.id)
                                .flatMap { mapping ->
                                    mediaItemRepository.findById(mapping.mediaItemId)
                                        .flatMap { item ->
                                            if (!newResIdSet.contains(item.resourceId)) {
                                                playlistItemMappingRepository
                                                    .deleteByPlaylistIdAndMediaItemId(
                                                        playlistId = current.id,
                                                        mediaItemId = mapping.mediaItemId,
                                                    )
                                            } else Mono.empty()
                                        }
                                }
                                .then()

                        /* ───────────────────── 4. 모든 작업 병렬 실행 후 플레이리스트 반환 ───────────────────── */
                        Mono.`when`(playlistMono, upsertItemsMono, deleteOldMappingsMono)
                            .then(playlistMono)
                    }
            }


    fun getSlice(
        size: Int,
        sortBy: String,
        direction: String,
        offset: Long,
    ): Mono<Slice<MediaPlaylist>> = mediaPlaylistRepository.findAllBy(
        size = size,
        sortBy = sortBy,
        direction = direction,
        offset = offset,
    )

    fun findAllById(ids: List<Long>): Flux<MediaPlaylist> = mediaPlaylistRepository.findAllById(ids)
}