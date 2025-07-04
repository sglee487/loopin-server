package com.loopin.media_catalog_service.domain.service

import com.loopin.media_catalog_service.domain.exception.AlreadyExistsException
import com.loopin.media_catalog_service.domain.exception.NotExistsException
import com.loopin.media_catalog_service.domain.model.MediaPlaylist
import com.loopin.media_catalog_service.domain.model.PlaylistItemMapping
import com.loopin.media_catalog_service.domain.repository.MediaItemRepository
import com.loopin.media_catalog_service.domain.repository.MediaItemWIthPositionRepository
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

@Service
class MediaPlaylistService(
    private val youtubeClient: YoutubeClient,
    private val mediaPlaylistRepository: MediaPlaylistRepository,
    private val mediaItemRepository: MediaItemRepository,
    private val playlistItemMappingRepository: PlaylistItemMappingRepository,
    private val mediaItemWIthPositionRepository: MediaItemWIthPositionRepository,
) {

    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    fun getByIdWithItems(id: Long): Mono<PlaylistResponseDto> =
        mediaPlaylistRepository.findById(id)
            .switchIfEmpty(Mono.error(IllegalArgumentException("playlist $id not found")))
            .flatMap { playlist ->
                mediaItemWIthPositionRepository
                    .findByPlaylistId(playlist.id!!)
                    .map {
                        logger.info("Found ${it.id} ${it.title} ${it.playlistPosition}")
                        it.toDto(position = it.playlistPosition)
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
                        logger.info(youtubeData.toString())

                        mediaPlaylistRepository.findByResourceId(youtubeData.playlist.resourceId)
                            .switchIfEmpty(mediaPlaylistRepository.save(youtubeData.playlist))
                            .flatMap { playlist ->
                                Flux.fromIterable(youtubeData.mediaItem)
                                    .index()
                                    .flatMap { indexedItem ->
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
                                                    playlistId = playlist.id!!,
                                                    mediaItemId = savedItem.id!!
                                                )
                                                    .switchIfEmpty(
                                                        playlistItemMappingRepository.save(
                                                            PlaylistItemMapping(
                                                                playlistId = playlist.id,
                                                                mediaItemId = savedItem.id,
                                                                position = index.toInt()
                                                            )
                                                        )
                                                    )
                                            }
                                    }.then(Mono.just(playlist))
                            }
                    }
            )

    fun updateByResourceId(resourceId: String): Mono<MediaPlaylist> =
        youtubeClient.getPlaylistWithItems(resourceId)
            .flatMap { youtubeData ->
                mediaPlaylistRepository.findByResourceId(resourceId)
                    .switchIfEmpty(Mono.error(NotExistsException("Playlist with resourceId $resourceId not found")))
                    .flatMap { current ->
                        /* 1) 메타데이터가 달라졌으면 갱신 */
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

                        /* 2) 항목 upsert + 매핑 upsert */
                        val newItems = youtubeData.mediaItem
                        val newResIdSet = newItems.map { it.resourceId }.toSet()

                        val upsertItemsMono = Flux.fromIterable(newItems)
                            .index()   // (index, MediaItem)
                            .flatMap { (idx, newItem) ->
                                mediaItemRepository.findByResourceId(newItem.resourceId)
                                    /* 없는 경우 저장 */
                                    .switchIfEmpty(mediaItemRepository.save(newItem))
                                    /* 존재하지만 내용이 달라지면 업데이트 */
                                    .flatMap { saved ->
                                        val needUpdate =
                                            saved.title != newItem.title ||
                                                    saved.description != newItem.description ||
                                                    saved.thumbnail != newItem.thumbnail ||
                                                    saved.durationSeconds != newItem.durationSeconds

                                        val finalItemMono =
                                            if (needUpdate) mediaItemRepository.save(
                                                saved.copy(
                                                    title = newItem.title,
                                                    description = newItem.description,
                                                    thumbnail = newItem.thumbnail,
                                                    durationSeconds = newItem.durationSeconds,
                                                )
                                            ) else Mono.just(saved)

                                        /* 매핑 upsert & position 수정 */
                                        finalItemMono.flatMap { finalItem ->
                                            playlistItemMappingRepository
                                                .findByPlaylistIdAndMediaItemId(
                                                    playlistId = current.id!!,
                                                    mediaItemId = finalItem.id!!,
                                                )
                                                /* 매핑 없으면 생성 */
                                                .switchIfEmpty(
                                                    playlistItemMappingRepository.save(
                                                        PlaylistItemMapping(
                                                            playlistId = current.id,
                                                            mediaItemId = finalItem.id,
                                                            position = idx.toInt(),
                                                        )
                                                    )
                                                )
                                                /* 매핑은 있지만 position 달라지면 갱신 */
                                                .flatMap { mapping ->
                                                    if (mapping!!.position != idx.toInt()) {
                                                        playlistItemMappingRepository.save(
                                                            mapping.copy(
                                                                position = idx.toInt(),
                                                            )
                                                        )
                                                    } else Mono.just(mapping)
                                                }
                                        }
                                    }
                            }
                            .then()   // Flux → Mono<Void>

                        /* 3) 사라진(=비공개/삭제) 항목 매핑 제거 */
                        val deleteOldMappingsMono =
                            playlistItemMappingRepository.findByPlaylistId(current.id!!)
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
                                .then()   // Mono<Void>

                        /* 모두 끝나면 최종 플레이리스트 반환 */
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