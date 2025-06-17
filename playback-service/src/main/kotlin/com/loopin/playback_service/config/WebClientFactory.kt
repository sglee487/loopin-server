package com.loopin.playback_service.config

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class WebClientFactory(
    private val builder: WebClient.Builder,
    private val clientProperties: ClientProperties
) {
    fun mediaCatalogServiceClient(): WebClient {
        return builder
            .baseUrl(clientProperties.mediaCatalogServiceUri.toString())
            .build()
    }
}