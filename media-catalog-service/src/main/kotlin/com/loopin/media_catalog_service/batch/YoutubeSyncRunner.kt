package com.loopin.media_catalog_service.batch

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("youtube-sync")
class YoutubeSyncRunner(
    private val youtubePlaylistSynchronizer: YoutubePlaylistSynchronizer
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String?) {
        logger.info("YoutubeSyncRunner started. Initiating YouTube playlist synchronization.")
        youtubePlaylistSynchronizer.runSynchronization()
            .doOnSuccess { logger.info("YouTube playlist synchronization completed successfully.") }
            .doOnError { e -> logger.error("YouTube playlist synchronization failed: ${e.message}", e) }
            .block() // CommandLineRunner는 블로킹 방식으로 동작해야 합니다.
    }
} 