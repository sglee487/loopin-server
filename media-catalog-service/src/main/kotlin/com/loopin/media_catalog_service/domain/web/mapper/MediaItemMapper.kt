package com.loopin.media_catalog_service.domain.web.mapper

import com.loopin.media_catalog_service.domain.model.MediaItem
import com.loopin.media_catalog_service.domain.repository.projection.MediaItemWithPosition
import com.loopin.media_catalog_service.domain.web.dto.MediaItemDto

fun MediaItem.toDto(position: Int? = null): MediaItemDto =
    MediaItemDto(
        id = id!!,
        resourceId = resourceId,
        title = title,
        description = description,
        kind = kind,
        publishedAt = publishedAt,
        thumbnail = thumbnail,
        channelId = videoOwnerChannelId,
        channelTitle = videoOwnerChannelTitle,
        platformType = platformType,
        durationSeconds = durationSeconds,
        position = position,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun MediaItemWithPosition.toDto(position: Int?) =
    MediaItemDto(
        id = id,
        resourceId = resourceId,
        title = title,
        description = description,
        kind = kind,
        publishedAt = publishedAt,
        thumbnail = thumbnail,
        channelId = videoOwnerChannelId,
        channelTitle = videoOwnerChannelTitle,
        platformType = platformType,
        durationSeconds = durationSeconds,
        position = position ?: -1   // null 방지
    )