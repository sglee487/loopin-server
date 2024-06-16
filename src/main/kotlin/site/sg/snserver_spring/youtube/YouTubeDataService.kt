package site.sg.snserver_spring.youtube

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.PlaylistItem
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.stereotype.Service
import java.io.IOException
import java.security.GeneralSecurityException


@Service
class YouTubeDataService(
    private val playListItemsRepository: PlayListItemsRepository
) {
    private val APPLICATION_NAME = "API code samples"
    private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()

    private val dotenv = Dotenv.load()

    // 환경 변수에서 API 키를 가져옵니다.
    private val DEVELOPER_KEY = dotenv["YOUTUBE_API_KEY"]
        ?: throw IllegalStateException("Missing YOUTUBE_API_KEY in environment variables")


    @Throws(GeneralSecurityException::class, IOException::class)
    fun getService(): YouTube {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        return YouTube.Builder(httpTransport, JSON_FACTORY, null)
            .setApplicationName(APPLICATION_NAME)
            .build()
    }

    @Throws(GeneralSecurityException::class, IOException::class, GoogleJsonResponseException::class)
    fun getplaylist(playlistId: String): PlayList {
        val youtubeService = getService()

        val playlistsRequest = youtubeService.playlists()
            .list(listOf("snippet", "contentDetails")).setKey(DEVELOPER_KEY)

        val playlistsResponse = playlistsRequest.setId(listOf(playlistId)).execute()

        val playList = PlayList(
            playListId = playlistsResponse.items[0].id,
            channelId = playlistsResponse.items[0].snippet.channelId,
            title = playlistsResponse.items[0].snippet.title,
            description = playlistsResponse.items[0].snippet.description,
            thumbnails = playlistsResponse.items[0].snippet.thumbnails,
            channelTitle = playlistsResponse.items[0].snippet.channelTitle,
            localized = playlistsResponse.items[0].snippet.localized,
            contentDetails = playlistsResponse.items[0].contentDetails,
            publishedAt = playlistsResponse.items[0].snippet.publishedAt
        )

        val playlistItemsRequest = youtubeService.playlistItems()
            .list(listOf("snippet"))

        val items = mutableListOf<PlaylistItem>()

        var nextPageToken: String? = null

        do {
            val playlistItemsResponse = playlistItemsRequest.setKey(DEVELOPER_KEY).setMaxResults(50L)
                .setPlaylistId(playlistId)
                .setPageToken(nextPageToken)
                .execute()

            items.addAll(playlistItemsResponse.items)
            nextPageToken = playlistItemsResponse.nextPageToken
        } while (nextPageToken != null)

        playList.items = items

        playListItemsRepository.save(playList)

        return playList
    }

}
