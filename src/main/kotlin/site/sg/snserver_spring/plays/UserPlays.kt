package site.sg.snserver_spring.plays

import jakarta.persistence.Column
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import site.sg.snserver_spring.youtube.ContentDetails
import site.sg.snserver_spring.youtube.Localized
import site.sg.snserver_spring.youtube.PlayListItem
import java.net.URL
import java.time.LocalDateTime
import java.util.*

@Document
class UserPlays(
    @Id val userId: UUID,
    val plays: Plays
) {
}

@Document
class Plays(
    var currentPlays:MutableMap<String, CurrentPlay>, // playlistId, current play list item
    var playListsQueues:MutableMap<String, PlayListQueues>, // playlistId, prev & next list items
)

@Document
class PlayLists(
    @Id val playListsId: String, // @Id must be initialized to decide duplicate or not


)

@Document
class PlayListQueues(
    var prev: MutableList<PlayListItem>,
    var next: MutableList<PlayListItem>

)

@Document
class CurrentPlay(
    var startSeconds: Float = 0.0f,
    val playListId: String,
    val channelId: String,
    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    var localized: Localized,
    var contentDetails: ContentDetails,
    var item: PlayListItem?,

    val publishedAt: LocalDateTime,
    var updatedAt: LocalDateTime

) {}

