package com.loopin.youtube_fetcher_service.media_catalog

data class PlaylistWithItems(
    val playlist: MediaPlaylist,
    val mediaItem: List<MediaItem>,
)
