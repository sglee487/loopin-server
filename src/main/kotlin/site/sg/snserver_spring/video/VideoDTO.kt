package site.sg.snserver_spring.video

import java.time.LocalDateTime

data class VideoDTO(
    val id: String?,
    val title: String,
    val uuid: String,
    val uploadedAt: LocalDateTime,
    val thumbnail: String,
    val videoInfo: VideoInfo,
    val author: String,
    val hit: Int,
    val like: Int,
    val dislike: Int,
    val description: String?
)