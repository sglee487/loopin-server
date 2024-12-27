package sg.snserver.hex.domain.entities

import sg.snserver.hex.domain.enums.PlatformType
import java.net.URL
import java.time.Instant

data class Playlist(
    val playlistId: String,
    val channelId: String,
    var title: String,

    var description: String?,
    var thumbnail: URL?,
    var channelTitle: String,

    var localized: Localized,
    var contentDetails: ContentDetails,

    val publishedAt: Instant,

    var items: MutableList<PlayItem>? = mutableListOf(),

    val platformType: PlatformType,
) : Base()
