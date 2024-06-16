package site.sg.snserver_spring.youtube

import com.google.api.client.util.DateTime
import com.google.api.services.youtube.model.PlaylistContentDetails
import com.google.api.services.youtube.model.PlaylistItem
import com.google.api.services.youtube.model.PlaylistLocalization
import com.google.api.services.youtube.model.ThumbnailDetails
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class PlayList(
    val playListId: String,
    val channelId: String,
    var title: String,
    var description: String,
    var thumbnails: ThumbnailDetails,
    var channelTitle: String,
    var localized: PlaylistLocalization,
    var contentDetails: PlaylistContentDetails,
    var items: List<PlaylistItem> = mutableListOf(),
    val publishedAt: DateTime,

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
