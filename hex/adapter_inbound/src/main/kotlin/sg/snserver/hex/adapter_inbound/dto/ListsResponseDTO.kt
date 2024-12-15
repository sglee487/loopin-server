package sg.snserver.hex.adapter_inbound.dto

import sg.snserver.hex.domain.entities.ContentDetails
import sg.snserver.hex.domain.entities.Localized
import java.net.URL
import java.time.LocalDateTime

data class ListsResponseDTO(
    val playListId: String,
    val channelId: String,
    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    var localized: Localized,
    var contentDetails: ContentDetails,
    val publishedAt: LocalDateTime,

//    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)