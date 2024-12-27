//package sg.snserver.hex.application.services
//
//import org.springframework.stereotype.Service
//import sg.snserver.hex.application.inbound.GetUserCurrentPlaysUseCase
//import sg.snserver.hex.application.outbound.GetUserPlaysPort
//import sg.snserver.hex.domain.entities.*
//import java.time.Instant
//import java.util.*
//
//@Service
//class UserPlaysService(
//    private val getUserPlaysPort: GetUserPlaysPort,
//) : GetUserCurrentPlaysUseCase {
//    override fun getUserCurrentPlays(userId: UUID): List<Map<String, CurrentPlay>> {
//        val results = getUserPlaysPort.getUserCurrentPlays(userId)
//
//        return listOf(
//            mapOf(
//                "asdf" to CurrentPlay(
//                    playlistId = "asdf33",
//                    playItem = NewPlayItem(
//                        publishedAt = Instant.now(),
//                        channelId = "1111",
//                        title = "tttt",
//                        description = null,
//                        thumbnail = null,
//                        channelTitle = "ccc",
//                        playListId = "111111",
//                        position = 4,
//                        resource = Resource(
//                            kind = "222",
//                            videoId = "444"
//                        ),
//                        videoOwnerChannelId = "dddddddd",
//                        videoOwnerChannelTitle = null,
//                    ),
//                    playlist = Playlist(
//                        playlistId = "ppppp",
//                        channelId = "2222",
//
//                        title = "tttt",
//                        description = null,
//                        thumbnail = null,
//                        channelTitle = "ccc",
//                        localized = Localized(
//                            title = "tttt",
//                            description = "tttt",
//                        ),
//                        contentDetails = ContentDetails(
//                            itemCount = 44
//                        ),
//                        publishedAt = Instant.now(),
//                    ),
//                    playlistQueues = PlayListQueues(
//                        prev = emptyList<NewPlayItem>().toMutableList(),
//                        next = emptyList<NewPlayItem>().toMutableList(),
//                    )
//                )
//            )
//        )
//    }
//}