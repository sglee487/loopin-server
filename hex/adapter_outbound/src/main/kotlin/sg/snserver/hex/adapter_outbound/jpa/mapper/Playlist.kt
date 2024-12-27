package sg.snserver.hex.adapter_outbound.jpa.mapper

import sg.snserver.hex.adapter_outbound.jpa.entities.*
import sg.snserver.hex.adapter_outbound.jpa.enums.PlatformTypeEntity
import sg.snserver.hex.domain.entities.*
import sg.snserver.hex.domain.enums.PlatformType


fun Localized.toEntity() = LocalizedEntity(
    videoId = playlistId,
    title = title,
    description = description,
)

fun Resource.toEntity() = ResourceEntity(
    kind = kind,
    videoId = videoId,
)

fun ContentDetails.toEntity() = ContentDetailsEntity(
    playlistId = playlistId,
    itemCount = itemCount,
)

fun PlatformType.toEntity() = PlatformTypeEntity.fromValue(value)