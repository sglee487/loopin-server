package sg.snserver.hex.adapter_outbound.youtube

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.ThumbnailDetails
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import sg.snserver.hex.adapter_outbound.config.YoutubeDataProperties
import sg.snserver.hex.application.outbound.LoadYoutubeDataPort
import sg.snserver.hex.domain.entities.*
import java.io.IOException
import java.net.URI
import java.net.URL
import java.security.GeneralSecurityException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Component
class YoutubeDataAdapter(
    private val youtubeDataProperties: YoutubeDataProperties,
): LoadYoutubeDataPort {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private val APPLICATION_NAME = "API code samples"

    private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()

    override fun loadYoutubeData(playlistId: String): Playlist {
        val youtubeService = getService()

        val playlistsRequest = youtubeService.playlists()
            .list(listOf("snippet", "contentDetails")).setKey(youtubeDataProperties.apiKey)

//        val playlistsResponse = playlistsRequest.setId(listOf(playlistId)).execute()
//
//        println(playlistsResponse)
//        log.info(playlistsResponse.toString())

        val playList = playlistsRequest.setId(listOf(playlistId)).execute().let {
            response ->
            Playlist(
                playlistId = response.items[0].id,
                channelId = response.items[0].snippet.channelId,
                title = response.items[0].snippet.title,
                description = response.items[0].snippet.description,
                thumbnail = response.items[0].snippet.thumbnails.getHighestThumbnail(),
                channelTitle = response.items[0].snippet.channelTitle,
                localized = Localized(
                    title = response.items[0].snippet.localized.title,
                    description = response.items[0].snippet.localized.description
                ),
                contentDetails = ContentDetails(
                    itemCount = response.items[0].contentDetails.itemCount
                ),
                publishedAt = response.items[0].snippet.publishedAt.toStringRfc3339().toLocalDateTime().toInstant(
                    ZoneOffset.UTC
                ),
            )
        }

        val playlistItemsRequest = youtubeService.playlistItems()
            .list(listOf("snippet"))

        val items = mutableListOf<NewPlayItem>()

        var nextPageToken: String? = null

        do {
            val playlistItemsResponse = playlistItemsRequest.setKey(youtubeDataProperties.apiKey).setMaxResults(50L)
                .setPlaylistId(playlistId)
                .setPageToken(nextPageToken)
                .execute()

            items.addAll(playlistItemsResponse.items.map {
                NewPlayItem(
                    publishedAt = it.snippet.publishedAt.toStringRfc3339().toLocalDateTime().toInstant(
                        ZoneOffset.UTC
                    ),
                    channelId = it.snippet.channelId,
                    title = it.snippet.title,
                    description = it.snippet.description,
                    thumbnail = it.snippet.thumbnails.getHighestThumbnail(),
                    channelTitle = it.snippet.channelTitle,
                    playListId = it.snippet.playlistId,
                    position = it.snippet.position,
                    resource = Resource(
                        kind = it.snippet.resourceId.kind,
                        videoId = it.snippet.resourceId.videoId
                    ),
                    videoOwnerChannelId = it.snippet.videoOwnerChannelId,
                    videoOwnerChannelTitle = it.snippet.videoOwnerChannelTitle,
                )
            })
            nextPageToken = playlistItemsResponse.nextPageToken
        } while (nextPageToken != null)

        playList.items = items

        return playList
    }

    @Throws(GeneralSecurityException::class, IOException::class)
    private fun getService(): YouTube {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        return YouTube.Builder(httpTransport, JSON_FACTORY, null)
            .setApplicationName(APPLICATION_NAME)
            .build()
    }

    private fun ThumbnailDetails.getHighestThumbnail(): URL? {
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