package sg.snserver.hex.adapter_inbound.dto

import java.net.URL
import java.time.Instant

data class PlayListItemResponseDTO(
    val publishedAt: Instant,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnail: URL?,
    val channelTitle: String,
    val playListId: String,
    val position: Long,
    val resource: ResourceResponseDTO,
    val videoOwnerChannelTitle: String?,
    val videoOwnerChannelId: String?,
)