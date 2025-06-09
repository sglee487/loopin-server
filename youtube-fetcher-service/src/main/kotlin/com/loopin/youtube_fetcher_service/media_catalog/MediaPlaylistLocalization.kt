package com.loopin.youtube_fetcher_service.media_catalog

data class MediaPlaylistLocalization(
    val mediaPlaylistId: String,
    val languageCode: String,
    val title: String,
    val description: String?
)
