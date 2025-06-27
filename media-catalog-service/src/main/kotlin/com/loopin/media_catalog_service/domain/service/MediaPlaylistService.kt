package com.loopin.media_catalog_service.domain.service

import com.loopin.media_catalog_service.domain.model.MediaPlaylist
import com.loopin.media_catalog_service.domain.model.PlaylistItemMapping
import com.loopin.media_catalog_service.domain.repository.*
import com.loopin.media_catalog_service.domain.web.dto.PlaylistResponseDto
import com.loopin.media_catalog_service.domain.web.mapper.toDto
import com.loopin.media_catalog_service.domain.web.mapper.toResponseDto
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
                        it.toDto(position = it.playlistPosition) }
                    .collectList()
                    .map { items -> playlist.toResponseDto(items) }
            }

    fun getByResourceId(resourceId: String): Mono<MediaPlaylist> {
        return mediaPlaylistRepository.findByResourceId(resourceId)
    }

    fun createByResourceId(resourceId: String): Mono<MediaPlaylist> {
        return youtubeClient.getPlaylistWithItems(resourceId)
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
}