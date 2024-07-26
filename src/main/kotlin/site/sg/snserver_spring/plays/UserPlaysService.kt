package site.sg.snserver_spring.plays

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserPlaysService(
    val userPlaysRepository: UserPlaysRepository
) {

    private val logger = LoggerFactory.getLogger(UserPlaysService::class.java)

    fun getUserPlays(userId: UUID): Optional<UserPlays> {
        val result = userPlaysRepository.findById(userId)
        logger.debug(result.toString())
        return result
    }

    fun createOrUpdateUserPlays(
        userId: UUID,
        playListId: String,
        currentPlay: CurrentPlay,
        playListsQueues: PlayListQueues
    ): UserPlays {

        val userPlays = userPlaysRepository.findById(userId).orElseGet {
            UserPlays(userId, Plays(mutableMapOf(), mutableMapOf()))
        }

        val currentPlays = userPlays.plays.currentPlays
        currentPlays[playListId] = currentPlay
        val playlistsQueues = userPlays.plays.playListsQueues
        playlistsQueues[playListId] = playListsQueues

        userPlaysRepository.save(userPlays)

        return userPlays
    }

}