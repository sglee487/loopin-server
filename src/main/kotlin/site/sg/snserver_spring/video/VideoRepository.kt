package site.sg.snserver_spring.video

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface VideoRepository: MongoRepository<Video, String> {
    fun findByUuid(uuid: String): Video

    @Query(value = "{}", fields = "{ 'id': 1, 'title': 1, 'uuid': 1, 'createdAt': 1, 'thumbnail': 1, 'videoInfo': 1, 'content': 1 }")
    fun findAllProjectedBy(pageable: Pageable): Page<VideoDTO>
}