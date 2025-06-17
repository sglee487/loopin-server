package com.loopin.media_catalog_service.domain.model

data class PlaylistWithItems(
    val playlist: MediaPlaylist,
    val mediaItem: List<MediaItem>,
    val playlistContentDetails: MediaPlaylistContentDetails,
)

