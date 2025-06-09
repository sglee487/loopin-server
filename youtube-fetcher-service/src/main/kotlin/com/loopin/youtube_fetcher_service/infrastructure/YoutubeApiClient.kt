package com.loopin.youtube_fetcher_service.infrastructure

import org.springframework.stereotype.Component
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.ThumbnailDetails
import com.loopin.youtube_fetcher_service.config.YoutubeDataProperties
import com.loopin.youtube_fetcher_service.media_catalog.MediaItem
import com.loopin.youtube_fetcher_service.media_catalog.MediaPlaylist
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import java.io.IOException
import java.net.URI
import java.net.URL
import java.security.GeneralSecurityException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Component
class YoutubeApiClient(
    private val youtubeDataProperties: YoutubeDataProperties,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val APPLICATION_NAME = "API code samples"

    private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()

    fun getMediaPlaylist(id: String): Mono<MediaPlaylist> {
        val youtubeService = getService()

        val playlistsRequest = youtubeService.playlists()
            .list(listOf("snippet", "contentDetails")).setKey(youtubeDataProperties.apiKey)

        val mediaPlaylist: MediaPlaylist = playlistsRequest.setId(listOf(id)).execute().let {
                response ->
            println(response)
            println(response.items)
            return@let MediaPlaylist(
                resourceId = response.items[0].id,
                thumbnail = response.items[0].snippet.thumbnails.getHighestThumbnail().toString(),
                channelId = response.items[0].snippet.channelId,
                channelTitle = response.items[0].snippet.channelTitle,
                publishedAt = response.items[0].snippet.publishedAt.toInstant(),
                platformType = response.items[0].kind
            )
        }


        val playlistItemsRequest = youtubeService.playlistItems()
            .list(listOf("snippet"))

        val items = mutableListOf<MediaItem>()

        var nextPageToken: String? = null

        do {
            println("=====================================================")
            val playlistItemsResponse = playlistItemsRequest.setKey(youtubeDataProperties.apiKey).setMaxResults(50L)
                .setPlaylistId(id)
                .setPageToken(nextPageToken)
                .execute()

            println(playlistItemsResponse.items)

//            items.addAll(playlistItemsResponse.items.map {
//                MediaItem(
//                    resourceId = it.snippet.resourceId.videoId,
//                    publishedAt = it.snippet.publishedAt.toInstant(),
////                    channelId = it.snippet.channelId,
////                    channelTitle = it.snippet.channelTitle,
////                    uploaderChannelId = it.snippet.videoOwnerChannelId,
//                    videoOwnerChannelId = it.snippet.videoOwnerChannelId,
//                    videoOwnerChannelTitle = it.snippet.videoOwnerChannelTitle,
//                    platformType = it.kind,
//
//
//                )
//            })
            nextPageToken = playlistItemsResponse.nextPageToken
        } while (nextPageToken != null)

        return TODO()
    }


    fun getMediaItem(id: String): Mono<MediaItem> {
        return TODO()
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


    fun DateTime.toInstant(): Instant = Instant.ofEpochMilli(this.value)

}