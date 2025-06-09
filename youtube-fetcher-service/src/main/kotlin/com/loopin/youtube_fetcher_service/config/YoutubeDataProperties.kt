package com.loopin.youtube_fetcher_service.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "youtube")
class YoutubeDataProperties(
    var apiKey: String = ""
)