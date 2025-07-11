package com.loopin.media_catalog_service

import com.redis.testcontainers.RedisContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    /* ─────────── PostgreSQL ─────────── */
    @Bean
    @ServiceConnection                 // → spring.datasource.* 자동 주입
    fun postgresContainer(): PostgreSQLContainer<*> =
        PostgreSQLContainer(DockerImageName.parse("postgres:14.12"))

    /* ─────────── Redis ─────────── */
    @Bean
    @ServiceConnection                 // → spring.redis.* 자동 주입
    fun redisContainer(): RedisContainer =
        RedisContainer(DockerImageName.parse("redis:8.0.2"))
            .withExposedPorts(6379)

}
