package site.sg.snserver_spring.youtube

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface PlayListItemsRepository : MongoRepository<PlayList, String> {

    fun findPlayListByPlayListId(playListId: String): PlayList?

    fun findAllProjectedBy(pageable: Pageable): Page<ListsDTO>
}