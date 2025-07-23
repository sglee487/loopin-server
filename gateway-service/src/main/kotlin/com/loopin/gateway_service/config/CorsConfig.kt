package com.loopin.gateway_service.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig(
    @Value("\${cors.allowed-origins:}") private val allowedOriginsRaw: String
) {
    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val origins: List<String> = allowedOriginsRaw
            .takeIf { it.isNotBlank() }
            ?.split(",")
            ?.map { it.trim() }
            ?: listOf("*")

        val corsConfig = CorsConfiguration().apply {
            setAllowedOrigins(origins)
            addAllowedHeader("*")
            addAllowedMethod("*")
            allowCredentials = true
            maxAge = 3600
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return CorsWebFilter(source)
    }
}