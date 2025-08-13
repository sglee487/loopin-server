package com.loopin.youtube_fetcher_service.infrastructure

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.ThumbnailDetails
import com.loopin.youtube_fetcher_service.config.YoutubeDataProperties
import com.loopin.youtube_fetcher_service.media_catalog.MediaItem
import com.loopin.youtube_fetcher_service.media_catalog.MediaPlaylist
import com.loopin.youtube_fetcher_service.media_catalog.PlaylistWithItems
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.io.IOException
import java.net.URI
import java.net.URL
import java.security.GeneralSecurityException
import java.time.Duration
import java.time.Instant

@Component
class YoutubeApiClient(
    private val youtubeDataProperties: YoutubeDataProperties,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val APPLICATION_NAME = "API code samples"

    private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()

    fun getMediaPlaylist(id: String): Mono<PlaylistWithItems> {
        val youtubeService = getService()

        val playlistsRequest = youtubeService.playlists()
            .list(listOf("snippet", "contentDetails")).setKey(youtubeDataProperties.apiKey)

        val mediaPlaylist = playlistsRequest.setId(
            listOf(id)
        ).execute().let { response ->
            MediaPlaylist(
                resourceId = response.items[0].id,
                title = response.items[0].snippet.title,
                description = response.items[0].snippet.description?.takeIf { it.isNotEmpty() },
                kind = response.items[0].kind,
                thumbnail = response.items[0].snippet.thumbnails.getHighestThumbnail().toString(),
                channelId = response.items[0].snippet.channelId,
                channelTitle = response.items[0].snippet.channelTitle,
                publishedAt = response.items[0].snippet.publishedAt.toInstant(),
                platformType = response.items[0].kind,
                itemCount = response.items[0].contentDetails.itemCount.toInt()

            )
        }

        logger.info(mediaPlaylist.toString())


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

            logger.info(playlistItemsResponse.toString())

            println(playlistItemsResponse.items)

            val videoListResponse =
                youtubeService.videos().list(listOf("snippet,contentDetails")).setKey(youtubeDataProperties.apiKey)
                    .setId(playlistItemsResponse.items.map { it.snippet.resourceId.videoId }).execute()

            items.addAll(videoListResponse.items.map {
                MediaItem(
                    resourceId = it.id,
                    title = it.snippet.title,
                    description = it.snippet.description,
                    kind = it.kind,
                    publishedAt = it.snippet.publishedAt.toInstant(),
                    thumbnail = it.snippet.thumbnails.getHighestThumbnail().toString(),
                    videoOwnerChannelId = it.snippet.channelId,
                    videoOwnerChannelTitle = it.snippet.channelTitle,
                    platformType = it.kind,
                    durationSeconds = Duration.parse(it.contentDetails.duration).seconds,
                    videoId = it.id,
                )
            })
            nextPageToken = playlistItemsResponse.nextPageToken
        } while (nextPageToken != null)

        return Mono.just(
            PlaylistWithItems(
                playlist = mediaPlaylist,
                mediaItem = items.toList()
            )
        )
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