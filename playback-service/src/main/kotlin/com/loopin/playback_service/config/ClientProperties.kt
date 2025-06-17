package com.loopin.playback_service.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI

@ConfigurationProperties(prefix = "loopin")
data class ClientProperties(
    val mediaCatalogServiceUri: URI
)
