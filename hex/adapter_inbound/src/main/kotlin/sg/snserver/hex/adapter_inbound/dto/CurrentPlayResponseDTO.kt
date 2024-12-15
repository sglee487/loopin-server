package sg.snserver.hex.adapter_inbound.dto

import java.time.Instant

data class CurrentPlayResponseDTO(
    val startSeconds: Float,
    val playListId: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val channelTitle: String,
    val localized: LocalizedResponseDTO,
    val contentDetails: ContentDetailsResponseDTO,
    val item: PlayListItemResponseDTO?,
    val publishedAt: Instant,
    val updatedAt: Instant,
)