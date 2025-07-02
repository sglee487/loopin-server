package com.loopin.playback_service.domain.repository

import com.loopin.playback_service.domain.model.PlaySession
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PlaySessionRepository : ReactiveCrudRepository<PlaySession, Long> {
    fun findAllByUserId(userId: String): Flux<PlaySession>
    fun findByUserIdAndMediaPlaylistId(userId: String, mediaPlaylistId: Long): Mono<PlaySession>

    @Modifying
    @Query(
        """
    UPDATE play_session
    SET start_seconds = :startSeconds,
        updated_at    = CURRENT_TIMESTAMP  -- 또는 now() / clock_timestamp()
    WHERE user_id = :userId
      AND media_playlist_id = :mediaPlaylistId
    """
    )
    fun updateStartSeconds(
        userId: String,
        mediaPlaylistId: Long,
        startSeconds: Int
    ): Mono<Int>
}