package site.sg.snserver_spring.youtube

import org.springframework.data.annotation.LastModifiedDate
import java.net.URL
import java.time.LocalDateTime

data class ListsDTO(
    val playListId: String,
    val channelId: String,
    var title: String,
    var description: String,
    var thumbnail: URL?,
    var channelTitle: String,
    var localized: Localized,
    var contentDetails: ContentDetails,
    val publishedAt: LocalDateTime,

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
}