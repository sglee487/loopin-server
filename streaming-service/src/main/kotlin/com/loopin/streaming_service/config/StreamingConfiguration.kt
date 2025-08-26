package com.loopin.streaming_service.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
@EnableR2dbcRepositories
@EnableR2dbcAuditing
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(StreamingProperties::class)
class StreamingConfiguration {

    @Bean
    fun videoProcessingExecutor(): ExecutorService {
        return Executors.newCachedThreadPool { task ->
            Thread(task, "video-processing").apply {
                isDaemon = true
            }
        }
    }
}