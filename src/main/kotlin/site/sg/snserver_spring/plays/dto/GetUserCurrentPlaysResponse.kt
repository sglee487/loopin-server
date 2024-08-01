package site.sg.snserver_spring.plays.dto

class CurrentPlayResponse(
    val startSeconds: Float,
    val playListId: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val channelTitle: String,
    val localized: LocalizedResponse,
    val contentDetails: ContentDetailsResponse,
    val item: PlayListItemResponse?,
    val publishedAt: String,
    val updatedAt: String,
) {
}

class ContentDetailsResponse(
    val itemCount: Long
) {

}

class LocalizedResponse(
    val title: String,
    val description: String
) {

}

class ResourceResponse(
    val kind: String,
    val videoId: String
)
