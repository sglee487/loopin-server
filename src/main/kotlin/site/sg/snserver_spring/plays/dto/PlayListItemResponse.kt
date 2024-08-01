package site.sg.snserver_spring.plays.dto

import java.net.URL
import java.time.LocalDateTime

class PlayListItemResponse(
    val publishedAt: LocalDateTime,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnail: URL?,
    val channelTitle: String,
    val playListId: String,
    val position: Long,
    val resource: ResourceResponse,
    val videoOwnerChannelTitle: String?,
    val videoOwnerChannelId: String?,
) {

}