package com.loopin.media_catalog_service.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI

@ConfigurationProperties(prefix = "loopin")
data class ClientProperties(
    val youtubeServiceUri: URI
)
