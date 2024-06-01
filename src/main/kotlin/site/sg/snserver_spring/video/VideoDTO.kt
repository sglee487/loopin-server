package site.sg.snserver_spring.video

import java.time.LocalDateTime

data class VideoDTO(
    val id: String?,
    val title: String,
    val uuid: String,
    val createdAt: LocalDateTime,
    val thumbnail: String,
    val videoInfo: VideoInfo,
    val content: String?
)