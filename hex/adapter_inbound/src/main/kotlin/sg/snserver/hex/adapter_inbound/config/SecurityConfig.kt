package sg.snserver.hex.adapter_inbound.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.client.RestTemplate
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private val jwtIssuer: String? = null

    @Value("\${cors.allowed-origins}")
    private lateinit var allowedOrigins: String

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .cors { it.configurationSource(corsConfigurationSource()) } // CORS 설정 추가
            .csrf { it.disable() } // 선택적으로 CSRF 비활성화
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) // JWT 변환기 설정
                }
            }
        return http.build()
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        return JwtAuthenticationConverter().apply {
            setJwtGrantedAuthoritiesConverter { jwt ->
                val resourceRoles = jwt.claims["resource_access"]
                    ?.let { it as Map<*, *> }
                    ?.get("snclient")
                    ?.let { it as Map<*, *> }
                    ?.get("roles")
                    ?.let { it as List<*> }
                    ?.map { "ROLE_${it.toString().uppercase()}" } // ROLE 접두사 추가
                    ?.map { SimpleGrantedAuthority(it) } ?: emptyList()

                resourceRoles
            }
        }
    }

    @Bean
    fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager("jwks")
    }

    @Bean
    fun jwtDecoder(cacheManager: CacheManager): JwtDecoder {
        val requestFactory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(3000)
            setReadTimeout(3000)
        }

        val restTemplate = RestTemplate(requestFactory)

        val jwkSetUri = "${jwtIssuer}/protocol/openid-connect/certs"

        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
            .restOperations(restTemplate)
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        val allowedOrigins = allowedOrigins.split(",").map { it.trim() }

        configuration.allowedOriginPatterns = allowedOrigins // allowedOrigins 직접 사용
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}
