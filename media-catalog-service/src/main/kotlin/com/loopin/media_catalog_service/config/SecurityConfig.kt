package com.loopin.media_catalog_service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .authorizeExchange { exchange ->
                exchange
                    .pathMatchers("/actuator/**").permitAll()
                    .pathMatchers(HttpMethod.GET,
                        "/api/v1/playlists",
                        "/api/v1/playlists/**").permitAll()
                    .anyExchange().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                // 기본 설정이면 Customizer.withDefaults() 또는 빈 블록 { } 둘 다 OK
                oauth2.jwt(Customizer.withDefaults())
                // 👉 커스터마이즈 예시:
//                 oauth2.jwt { jwtSpec ->
//                     jwtSpec.jwtAuthenticationConverter(myConverter)
//                 }
            }

            .requestCache { it.requestCache(NoOpServerRequestCache.getInstance()) }
            .csrf { it.disable() }
            .build()

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_")
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles")

        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter)
        return jwtAuthenticationConverter
    }
}