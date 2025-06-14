package com.loopin.youtube_fetcher_service.media_catalog

data class PlaylistItemMapping(
    val playlistId: Long,
    val mediaItemId: Long,
    val position: Int
)
