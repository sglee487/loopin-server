package com.loopin.streaming_service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .authorizeExchange { exchanges ->
                exchanges
                    .pathMatchers("/actuator/**").permitAll()
                    .pathMatchers("/api/v1/hooks/**").permitAll()
                    .pathMatchers("/api/v1/streams/**").permitAll()
                    .pathMatchers("/api/v1/streams/*/playlist.m3u8").permitAll()
                    .pathMatchers("/api/v1/streams/*/*/playlist.m3u8").permitAll()
                    .pathMatchers("/api/v1/streams/*/*/*.ts").permitAll()
                    .anyExchange().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { }
            }
            .cors { cors ->
                cors.configurationSource { request ->
                    val config = org.springframework.web.cors.CorsConfiguration()
                    config.allowedOrigins = listOf("*")
                    config.allowedMethods = listOf("*")
                    config.allowedHeaders = listOf("*")
                    config.allowCredentials = false
                    config
                }
            }
            .csrf { it.disable() }
            .build()
    }
}