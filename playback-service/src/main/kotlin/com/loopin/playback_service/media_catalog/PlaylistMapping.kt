package com.loopin.playback_service.media_catalog

data class PlaylistItemMapping(
    val id: Long? = null,

    val playlistId: Long,
    val mediaItemId: Long,

    val position: Int,
)