package site.sg.snserver_spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests{ authorize ->
                authorize
                    .requestMatchers( "/swagger-ui/**","/api/v1/list/**","/api/v1/video/videos", "/actuator/**", "/usage" ).permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer{ oauth2ResourceServer ->
                oauth2ResourceServer.jwt { jwtConfigure ->
                    jwtConfigure.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }
        return http.build()
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtGrantedAuthoritiesConverter()
//        converter.setAuthoritiesClaimName("roles")
//        converter.setAuthorityPrefix("manage-account")

        val jwtConverter= JwtAuthenticationConverter()
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter)
        return jwtConverter
    }
}