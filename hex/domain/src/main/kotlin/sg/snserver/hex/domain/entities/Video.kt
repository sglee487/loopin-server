//package sg.snserver.hex.domain.entities
//
//import java.time.Instant
//import java.time.LocalDateTime
//import java.util.UUID
//
//data class Video(
//    var title: String,
//    val uuid: String,
//    val uploadedAt: Instant = Instant.now(),
//
//    var masterIndex: String,
//
//    var res480p: Map<String, String>,
//    var res360p: Map<String, String>,
//    var res240p: Map<String, String>,
//
//    var thumbnail: String, // base64
//    var videoInfo: VideoInfo,
//
//    val author: String,
//    var hit: Int = 0,
//    var like: Int = 0,
//    var dislike: Int = 0,
//
//    var description: String? = null,
//): Base()
