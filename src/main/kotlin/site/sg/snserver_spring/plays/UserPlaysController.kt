package site.sg.snserver_spring.plays

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import site.sg.snserver_spring.plays.dto.*
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

    @GetMapping("/current-plays")
    @ResponseStatus(HttpStatus.OK)
    fun getUserCurrentPlays(
        principal: Principal
    ): Map<String, CurrentPlayResponse> {
        val userId = UUID.fromString(principal.name)

        val userPlays: UserPlays? = userPlaysService.getUserPlays(userId)

        return userPlays?.plays?.currentPlays?.mapValues { entry ->
            CurrentPlayResponse(
                startSeconds = entry.value.startSeconds,
                playListId = entry.value.playListId,
                channelId = entry.value.channelId,
                title = entry.value.title,
                description = entry.value.description,
                thumbnail = entry.value.thumbnail.toString(),
                channelTitle = entry.value.channelTitle,
                localized = LocalizedResponse(
                    title = entry.value.localized.title,
                    description = entry.value.localized.description
                ),
                contentDetails = ContentDetailsResponse(
                    itemCount = entry.value.contentDetails.itemCount
                ),
                item = entry.value.item?.let { item ->
                    PlayListItemResponse(
                        publishedAt = item.publishedAt,
                        channelId = item.channelId,
                        title = item.title,
                        description = item.description,
                        thumbnail = item.thumbnail,
                        channelTitle = item.channelTitle,
                        playListId = item.playListId,
                        position = item.position,
                        resource = ResourceResponse(
                            kind = item.resource.kind,
                            videoId = item.resource.videoId
                        ),
                        videoOwnerChannelTitle = item.videoOwnerChannelTitle,
                        videoOwnerChannelId = item.videoOwnerChannelId
                    )
                },
                publishedAt = entry.value.publishedAt.toString(),
                updatedAt = entry.value.updatedAt.toString()
            )
        } ?: emptyMap()
    }

    @GetMapping("/playlist-queues/{playListId}")
    @ResponseStatus(HttpStatus.OK)
    fun getUserPlayListQueues(
        @PathVariable playListId: String,
        principal: Principal
    ): GetUserPlaylistQueuesResponse {
        val userId = UUID.fromString(principal.name)

        val userPlays: UserPlays? = userPlaysService.getUserPlays(userId)

        return GetUserPlaylistQueuesResponse(
            prev = createPlayListQueueItem(userPlays?.plays?.playListsQueues?.get(playListId)?.prev),
            next = createPlayListQueueItem(userPlays?.plays?.playListsQueues?.get(playListId)?.next)
        )
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

        userPlaysService.createOrUpdateUserPlays(
            userId,
            createOrUpdateUserPlaysRequest.playListId,
            currentPlay,
            playListQueues
        )
    }

    fun createPlayListQueueItem(
        playListItems: MutableList<PlayListItem>?
    ): List<PlayListItemResponse> {
        return playListItems?.map { item ->
            PlayListItemResponse(
                publishedAt = item.publishedAt,
                channelId = item.channelId,
                title = item.title,
                description = item.description,
                thumbnail = item.thumbnail,
                channelTitle = item.channelTitle,
                playListId = item.playListId,
                position = item.position,
                resource = ResourceResponse(
                    kind = item.resource.kind,
                    videoId = item.resource.videoId
                ),
                videoOwnerChannelTitle = item.videoOwnerChannelTitle,
                videoOwnerChannelId = item.videoOwnerChannelId
            )
        } ?: emptyList()
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