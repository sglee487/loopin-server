package com.loopin.youtube_fetcher_service.infrastructure

import org.springframework.stereotype.Component
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.ThumbnailDetails
import com.loopin.youtube_fetcher_service.media_catalog.MediaPlaylist
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import java.io.IOException
import java.net.URI
import java.net.URL
import java.security.GeneralSecurityException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Component
class YoutubeApiClient {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val APPLICATION_NAME = "API code samples"

    private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()

    fun getMediaPlaylist(id: String): Mono<MediaPlaylist> {

    }
}