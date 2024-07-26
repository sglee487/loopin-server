package site.sg.snserver_spring.plays

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import site.sg.snserver_spring.plays.dto.CreateOrUpdateUserPlaysRequest
import site.sg.snserver_spring.plays.dto.CurrentPlayRequest
import site.sg.snserver_spring.plays.dto.PlayListItemRequest
import site.sg.snserver_spring.plays.dto.PlayListQueuesRequest
import site.sg.snserver_spring.youtube.ContentDetails
import site.sg.snserver_spring.youtube.Localized
import site.sg.snserver_spring.youtube.PlayListItem
import site.sg.snserver_spring.youtube.Resource
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/api/v1/user_plays")
class UserPlaysController(
    val userPlaysService: UserPlaysService
) {

    private val logger = LoggerFactory.getLogger(UserPlaysController::class.java)

    @GetMapping
    fun getUserPlays(
        principal: Principal
    ): ResponseEntity<Any> {
        println(principal)
        println(principal.name)
        val userId = UUID.fromString(principal.name)

        val result = userPlaysService.getUserPlays(userId)

        logger.debug(result.toString())


        return ResponseEntity.ok(userId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrUpdateUserPlays(
        @RequestBody createOrUpdateUserPlaysRequest: CreateOrUpdateUserPlaysRequest,
        principal: Principal
    ) {
        val userId = UUID.fromString(principal.name)

        val currentPlay = createCurrentPlay(createOrUpdateUserPlaysRequest.currentPlays)
        val playListQueues = createPlayListQueues(createOrUpdateUserPlaysRequest.playListQueues)

        userPlaysService.createOrUpdateUserPlays(userId, createOrUpdateUserPlaysRequest.playListId, currentPlay, playListQueues)
    }

    fun createCurrentPlay(currentPlayRequest: CurrentPlayRequest): CurrentPlay {
        return CurrentPlay(
            startSeconds = currentPlayRequest.startSeconds,
            playListId = currentPlayRequest.playListId,
            channelId = currentPlayRequest.channelId,
            title = currentPlayRequest.title,
            description = currentPlayRequest.description,
            thumbnail = currentPlayRequest.thumbnail,
            channelTitle = currentPlayRequest.channelTitle,
            localized = Localized(
                title = currentPlayRequest.localized.title,
                description = currentPlayRequest.localized.description
            ),
            contentDetails = ContentDetails(
                itemCount = currentPlayRequest.contentDetails.itemCount
            ),
            item = createPlayListItem(currentPlayRequest.item),
            publishedAt = currentPlayRequest.publishedAt,
            updatedAt = currentPlayRequest.updatedAt
        )
    }

    fun createPlayListQueues(playListQueuesRequest: PlayListQueuesRequest): PlayListQueues {
        return PlayListQueues(
            prev = playListQueuesRequest.prev.map { createPlayListItem(it) }.toMutableList(),
            next = playListQueuesRequest.next.map { createPlayListItem(it) }.toMutableList()
        )
    }

    fun createPlayListItem(playListItemRequest: PlayListItemRequest): PlayListItem {
        return PlayListItem(
            publishedAt = playListItemRequest.publishedAt,
            channelId = playListItemRequest.channelId,
            title = playListItemRequest.title,
            description = playListItemRequest.description,
            thumbnail = playListItemRequest.thumbnail,
            channelTitle = playListItemRequest.channelTitle,
            playListId = playListItemRequest.playListId,
            position = playListItemRequest.position,
            resource = Resource(
                kind = playListItemRequest.resource.kind,
                videoId = playListItemRequest.resource.videoId
            ),
            videoOwnerChannelTitle = playListItemRequest.videoOwnerChannelTitle,
            videoOwnerChannelId = playListItemRequest.videoOwnerChannelId
        )
    }
}