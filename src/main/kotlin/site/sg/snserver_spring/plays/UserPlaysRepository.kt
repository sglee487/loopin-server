package site.sg.snserver_spring.plays

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface UserPlaysRepository : MongoRepository<UserPlays, UUID> {


}