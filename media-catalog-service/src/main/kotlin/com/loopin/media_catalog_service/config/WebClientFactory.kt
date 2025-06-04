package com.loopin.media_catalog_service.config

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class WebClientFactory(
    private val builder: WebClient.Builder,
    private val clientProperties: ClientProperties
) {
    fun youtubeClient(): WebClient {
        return builder
            .baseUrl(clientProperties.youtubeServiceUri.toString())
            .build()
    }
}