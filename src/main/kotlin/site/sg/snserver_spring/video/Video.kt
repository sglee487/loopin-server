package site.sg.snserver_spring.video

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.bson.types.Binary
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document
class Video(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String? = null,

    var title: String,
    val uuid: String = UUID.randomUUID().toString(),
    val createdAt: LocalDateTime = LocalDateTime.now(),

    var masterIndex: Binary,

    var res480p: Map<String, Binary>,
    var res360p: Map<String, Binary>,
    var res240p: Map<String, Binary>
) {
}