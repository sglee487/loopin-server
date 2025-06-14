package com.loopin.media_catalog_service.media_catalog.domain.web.dto

data class CreatePlaylistRequestDto(
    val type: String,
    val resourceId: String,
)
