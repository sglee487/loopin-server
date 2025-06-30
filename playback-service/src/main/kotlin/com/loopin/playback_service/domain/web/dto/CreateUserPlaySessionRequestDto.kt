package com.loopin.playback_service.domain.web.dto

import com.loopin.playback_service.domain.model.PlaySession

data class CreateUserPlaySessionRequestDto(
    val nowPlayingItemId: Long,
    val startSeconds: Int = 0,
    val prevItems: List<Long>,
    val nextItems: List<Long>,
) {
    fun toDomain(
        userId: String,
        mediaPlaylistId: Long,
    ) = PlaySession(
        userId = userId,
        mediaPlaylistId = mediaPlaylistId,
        nowPlayingItemId = nowPlayingItemId,
        startSeconds = 0,
        prevItems = prevItems,
        nextItems = nextItems,
    )
}