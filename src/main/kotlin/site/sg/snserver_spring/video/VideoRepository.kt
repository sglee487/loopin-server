package site.sg.snserver_spring.video

import org.springframework.data.mongodb.repository.MongoRepository

interface VideoRepository: MongoRepository<Video, String> {
    fun findByUuid(uuid: String): Video
}