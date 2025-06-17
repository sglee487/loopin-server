package com.loopin.media_catalog_service.domain.web.dto

data class CreatePlaylistRequestDto(
    val type: String,
    val resourceId: String,
)
