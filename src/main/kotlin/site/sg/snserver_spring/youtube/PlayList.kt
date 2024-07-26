package site.sg.snserver_spring.youtube

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.net.URL
import java.time.LocalDateTime

@Document
class PlayList(
    @Id val playListId: String, // @Id must be initialized to decide duplicate or not (here, with playListId)
    val channelId: String,
    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    var localized: Localized,
    var contentDetails: ContentDetails,
    var items: MutableList<PlayListItem> = mutableListOf(),
    val publishedAt: LocalDateTime,

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
}

class Localized(
    val title: String,
    val description: String
)

class ContentDetails(
    var itemCount: Long
)

class PlayListItem(
    val publishedAt: LocalDateTime,
    val channelId: String,
    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    val playListId: String,
    var position: Long,
    val resource: Resource,
    var videoOwnerChannelTitle: String?,
    val videoOwnerChannelId: String?,
)

class Resource(
    val kind: String,
    val videoId: String
)