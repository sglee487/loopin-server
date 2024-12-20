package sg.snserver.hex.adapter_outbound.jpa.dto

import sg.snserver.hex.domain.entities.ContentDetails
import sg.snserver.hex.domain.entities.Localized
import sg.snserver.hex.domain.entities.NewPlayItem
import sg.snserver.hex.domain.entities.Playlist
import java.net.URL
import java.time.Instant


data class PlaylistBatchDTO(
    val playlistId: String,
    val channelId: String,

    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    var localized: Localized,
    var contentDetails: ContentDetails,
    val publishedAt: Instant,
) {
    fun toDomain(): Playlist = Playlist(
        playlistId = playlistId,
        channelId = channelId,
        title = title,
        description = description,
        thumbnail = thumbnail,
        channelTitle = channelTitle,
        localized = localized,
        contentDetails = contentDetails,
        publishedAt = publishedAt,
        items = null,
    )
}