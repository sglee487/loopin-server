package site.sg.snserver_spring.youtube

import org.springframework.data.mongodb.repository.MongoRepository

interface PlayListItemsRepository : MongoRepository<PlayList, String> {

    fun findPlayListByPlayListId(playListId: String): PlayList?
}