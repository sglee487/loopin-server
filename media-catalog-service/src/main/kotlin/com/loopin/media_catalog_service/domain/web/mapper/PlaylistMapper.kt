package com.loopin.media_catalog_service.domain.web.mapper

import com.loopin.media_catalog_service.domain.model.MediaPlaylist
import com.loopin.media_catalog_service.domain.web.dto.MediaItemDto
import com.loopin.media_catalog_service.domain.web.dto.PlaylistResponseDto

fun MediaPlaylist.toDto(items: List<MediaItemDto>) =
    PlaylistResponseDto(
        id = id!!,
        resourceId = resourceId,
        title = title,
        description = description,
        kind = kind,
        thumbnail = thumbnail,
        channelId = channelId,
        channelTitle = channelTitle,
        publishedAt = publishedAt,
        platformType = platformType,
        itemCount = itemCount,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy,
        updatedBy = updatedBy,
        items = items,
    )