package sg.snserver.hex.domain.entities

import java.net.URL
import java.time.Instant

data class NewPlayItem(
    val publishedAt: Instant,
    val channelId: String,
    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    val playListId: String,
    var position: Long,
    val resource: Resource,
    val videoOwnerChannelId: String?,
    var videoOwnerChannelTitle: String?,
    var startSeconds: Float = 0.0f,
): Base()
