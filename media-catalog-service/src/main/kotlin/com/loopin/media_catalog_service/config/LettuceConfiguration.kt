package com.loopin.media_catalog_service.config

import io.lettuce.core.RedisClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class LettuceConfiguration {
    @Bean
    fun redisClient(
        @Value("\${spring.data.redis.host}") host: String,
        @Value("\${spring.data.redis.port}") port: String,
        @Value("\${spring.data.redis.password:}") password: String?
    ): RedisClient {
        val uri = if (password.isNullOrBlank()) {
            "redis://$host:$port"
        } else {
            "redis://$password@$host:$port"
        }
        return RedisClient.create(uri)
    }
}