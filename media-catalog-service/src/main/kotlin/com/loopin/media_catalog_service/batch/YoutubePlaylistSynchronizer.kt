package com.loopin.media_catalog_service.batch

import com.loopin.media_catalog_service.domain.repository.MediaPlaylistRepository
import com.loopin.media_catalog_service.domain.service.MediaPlaylistService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class YoutubePlaylistSynchronizer(
    private val mediaPlaylistRepository: MediaPlaylistRepository,
    private val mediaPlaylistService: MediaPlaylistService,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun runSynchronization(): Mono<Void> {
        return synchronizePlaylists()
    }

    private fun synchronizePlaylists(): Mono<Void> {
        logger.info("Starting YouTube playlist synchronization.")

        return mediaPlaylistRepository.findAllByKind("youtube#playlist")
            .flatMap { existingPlaylist ->
                mediaPlaylistService.updateByResourceId(resourceId = existingPlaylist.resourceId)
            }
            .then(Mono.fromRunnable { logger.info("YouTube playlist synchronization finished.") })
    }
} 