package site.sg.snserver_spring.youtube

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.ThumbnailDetails
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.URI
import java.net.URL
import java.security.GeneralSecurityException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class YouTubeDataService(
    private val playListItemsRepository: PlayListItemsRepository
) {
    private val APPLICATION_NAME = "API code samples"
    private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()

    private val dotenv = Dotenv.load()

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
    fun getplaylist(playlistId: String, refresh: Boolean): PlayList {
        val youtubeService = getService()

        val playlistsRequest = youtubeService.playlists()
            .list(listOf("snippet", "contentDetails")).setKey(DEVELOPER_KEY)

        val playList: PlayList = playListItemsRepository.findPlayListByPlayListId(playlistId)?.apply {
            if (!refresh) {
                return this
            }

            val playlistsResponse = playlistsRequest.setId(listOf(playlistId)).execute()
            this.title = playlistsResponse.items[0].snippet.title
            this.description = playlistsResponse.items[0].snippet.description
            this.thumbnail = playlistsResponse.items[0].snippet.thumbnails.getHightestThumbnail()
            this.channelTitle = playlistsResponse.items[0].snippet.channelTitle
            this.localized = Localized(
                title = playlistsResponse.items[0].snippet.localized.title,
                description = playlistsResponse.items[0].snippet.localized.description
            )
            this.contentDetails = ContentDetails(
                itemCount = playlistsResponse.items[0].contentDetails.itemCount
            )
        } ?: run {

            val playlistsResponse = playlistsRequest.setId(listOf(playlistId)).execute()
            PlayList(
                playListId = playlistsResponse.items[0].id,
                channelId = playlistsResponse.items[0].snippet.channelId,
                title = playlistsResponse.items[0].snippet.title,
                description = playlistsResponse.items[0].snippet.description,
                thumbnail = playlistsResponse.items[0].snippet.thumbnails.getHightestThumbnail(),
                channelTitle = playlistsResponse.items[0].snippet.channelTitle,
                localized = Localized(
                    title = playlistsResponse.items[0].snippet.localized.title,
                    description = playlistsResponse.items[0].snippet.localized.description
                ),
                contentDetails = ContentDetails(
                    itemCount = playlistsResponse.items[0].contentDetails.itemCount
                ),
                publishedAt = playlistsResponse.items[0].snippet.publishedAt.toStringRfc3339().toLocalDateTime()
            )
        }

        val playlistItemsRequest = youtubeService.playlistItems()
            .list(listOf("snippet"))

        val playItems = mutableListOf<PlayItem>()

        var nextPageToken: String? = null

        do {
            val playlistItemsResponse = playlistItemsRequest.setKey(DEVELOPER_KEY).setMaxResults(50L)
                .setPlaylistId(playlistId)
                .setPageToken(nextPageToken)
                .execute()

            playItems.addAll(playlistItemsResponse.items.map {
                PlayItem(
                    publishedAt = it.snippet.publishedAt.toStringRfc3339().toLocalDateTime(),
                    channelId = it.snippet.channelId,
                    title = it.snippet.title,
                    description = it.snippet.description,
                    thumbnail = it.snippet.thumbnails.getHightestThumbnail(),
                    channelTitle = it.snippet.channelTitle,
                    playListId = it.snippet.playlistId,
                    position = it.snippet.position,
                    resource = Resource(
                        kind = it.snippet.resourceId.kind,
                        videoId = it.snippet.resourceId.videoId
                    ),
                    videoOwnerChannelTitle = it.snippet.videoOwnerChannelTitle,
                    videoOwnerChannelId = it.snippet.videoOwnerChannelId
                )
            })
            nextPageToken = playlistItemsResponse.nextPageToken
        } while (nextPageToken != null)

        playList.items = playItems

        playListItemsRepository.save(playList)

        return playList
    }

    private fun ThumbnailDetails.getHightestThumbnail(): URL? {
        return if (this.maxres != null) {
            URI(this.maxres.url).toURL()
        } else if (this.standard != null) {
            URI(this.standard.url).toURL()
        } else if (this.high != null) {
            URI(this.high.url).toURL()
        } else if (this.medium != null) {
            URI(this.medium.url).toURL()
        } else if (this.default != null) {
            URI(this.default.url).toURL()
        } else {
            null
        }
    }

    private fun String.toLocalDateTime(): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return LocalDateTime.parse(this, formatter)
    }

}
