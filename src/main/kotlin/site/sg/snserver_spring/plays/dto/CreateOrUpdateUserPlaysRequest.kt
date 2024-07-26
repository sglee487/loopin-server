package site.sg.snserver_spring.plays.dto

import java.net.URL
import java.time.LocalDateTime

class CreateOrUpdateUserPlaysRequest(
    val playListId: String,
    val playListQueues: PlayListQueuesRequest,
    val currentPlays: CurrentPlayRequest
) {
}

class PlayListQueuesRequest(
    var prev: MutableList<PlayListItemRequest>,
    var next: MutableList<PlayListItemRequest>
)

class PlayListItemRequest(
    val publishedAt: LocalDateTime,
    val channelId: String,
    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    val playListId: String,
    var position: Long,
    val resource: ResourceRequest,
    var videoOwnerChannelTitle: String?,
    val videoOwnerChannelId: String?,
)

class ResourceRequest(
    val kind: String,
    val videoId: String
)

class CurrentPlayRequest(

    /*



     */

    var startSeconds: Float = 0.0f, //
    val playListId: String, //
    val channelId: String, //
    var title: String, //
    var description: String, //
    var thumbnail: URL?, //
    var channelTitle: String, //
    var localized: LocalizedRequest, //
    var contentDetails: ContentDetailsRequest, //
    var item: PlayListItemRequest, //

    val publishedAt: LocalDateTime, //
    var updatedAt: LocalDateTime = LocalDateTime.now() //
)

class LocalizedRequest(
    val title: String,
    val description: String
)

class ContentDetailsRequest(
    var itemCount: Long
)
