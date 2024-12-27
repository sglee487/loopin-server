package sg.snserver.hex.domain.entities

import sg.snserver.hex.domain.enums.PlatformType
import java.net.URL
import java.time.Instant

data class PlayItem(
    val videoId: String,

    val publishedAt: Instant,
    val channelId: String,
    var title: String,
    var description: String?,
    var thumbnail: URL?,
    var channelTitle: String,
    var position: Long,
    val resource: Resource,
    val videoOwnerChannelId: String?,
    var videoOwnerChannelTitle: String?,
    var startSeconds: Float = 0.0f,
    var isDeleted: Boolean = false,
    val platformType: PlatformType,
): Base()
