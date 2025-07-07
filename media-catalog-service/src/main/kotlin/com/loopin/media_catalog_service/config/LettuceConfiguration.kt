package com.loopin.media_catalog_service.config

import io.lettuce.core.RedisClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class LettuceConfiguration {
    @Bean
    fun redisClient(@Value("\${spring.data.redis.port}") port: String?): RedisClient {
        return RedisClient.create("redis://password@localhost:%s/".formatted(port))
    }
}