package com.loopin.youtube_fetcher_service.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "youtube")
class YoutubeDataProperties(
    var apiKey: String = "",
)