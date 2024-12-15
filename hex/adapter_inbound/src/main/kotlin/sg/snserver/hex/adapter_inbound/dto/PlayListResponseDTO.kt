package sg.snserver.hex.adapter_inbound.dto

import java.net.URL
import java.time.Instant
import java.time.LocalDateTime

data class PlayListResponseDTO(
    val playListId: String,
    val channelId: String,
    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    var localized: LocalizedResponseDTO,
    var contentDetails: ContentDetailsResponseDTO,
    var items: MutableList<PlayListItemResponseDTO> = mutableListOf(),
    val publishedAt: Instant,

    var updatedAt: Instant,
)
