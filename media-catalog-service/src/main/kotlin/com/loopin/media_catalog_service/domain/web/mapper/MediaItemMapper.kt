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
        videoOwnerChannelId = videoOwnerChannelId,
        videoOwnerChannelTitle = videoOwnerChannelTitle,
        platformType = platformType,
        durationSeconds = durationSeconds,
        position = position,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy,
        updatedBy = updatedBy,
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
        videoOwnerChannelId = videoOwnerChannelId,
        videoOwnerChannelTitle = videoOwnerChannelTitle,
        platformType = platformType,
        durationSeconds = durationSeconds,
        position = position ?: -1,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy,
        updatedBy = updatedBy,
    )