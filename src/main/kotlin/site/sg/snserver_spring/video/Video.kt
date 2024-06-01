package site.sg.snserver_spring.video

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class Video(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String? = null,

    var title: String,
    val uuid: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),

    var masterIndex: String,

    var res480p: Map<String, String>,
    var res360p: Map<String, String>,
    var res240p: Map<String, String>,

    var thumbnail: String, // base64
    var videoInfo: VideoInfo,

    var description: String? = null,
) {
}

class VideoInfo(
    val duration: Double,
)