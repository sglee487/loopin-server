package com.loopin.youtube_fetcher_service.media_catalog

data class MediaItemLocalization(
    val mediaItemId: String,
    val languageCode: String,
    val title: String,
    val description: String?
)
