package com.loopin.playback_service.domain.service

import com.loopin.playback_service.domain.model.PlaySession
import com.loopin.playback_service.domain.repository.PlaySessionRepository
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

    fun getUserPlaySessions(
        userId: String
    ): Flux<PlaySession> {
        logger.info(userId)
        return playSessionRepository.findAllByUserId(userId)
    }

}