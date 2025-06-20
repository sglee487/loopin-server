package com.loopin.media_catalog_service.domain.web.dto

data class SliceResponse<T>(
    val items: List<T>,
    val hasNext: Boolean
)