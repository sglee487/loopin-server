package site.sg.snserver_spring.video

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface VideoRepository : MongoRepository<Video, String> {
    fun findByUuid(uuid: String): Video
    fun findDTOByUuid(uuid: String): VideoDTO

    fun findAllProjectedBy(pageable: Pageable): Page<VideoDTO>
}