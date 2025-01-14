package sg.snserver.hex.adapter_inbound.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
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
}
