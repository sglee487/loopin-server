package com.loopin.gateway_service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository
import org.springframework.security.web.server.csrf.CsrfToken
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestAttributeHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.server.WebFilter
import org.springframework.web.server.session.CookieWebSessionIdResolver
import org.springframework.web.server.session.WebSessionIdResolver
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    /** OAuth2 Client 정보를 WebSession에 보관하도록 설정 */
    @Bean
    fun authorizedClientRepository(): ServerOAuth2AuthorizedClientRepository =
        WebSessionServerOAuth2AuthorizedClientRepository()

//    @Bean
//    fun webSessionIdResolver(): WebSessionIdResolver {
//        val resolver = CookieWebSessionIdResolver()
//        resolver.addCookieInitializer { builder ->
//            builder.sameSite("None").secure(true)
//        }
//        return resolver
//    }

    /**
     * Spring Security 필터 체인.
     * 공개 경로와 보호 경로를 구분하고, OIDC 로그인/로그아웃을 활성화합니다.
     */
    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        clientRegistrationRepository: ReactiveClientRegistrationRepository
    ): SecurityWebFilterChain = http
        .authorizeExchange { exchange ->
            exchange
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()   // ★추가
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/", "/*.css", "/*.js", "/favicon.ico").permitAll()
                // ⬇️ 프로젝트에 맞춰 공개 GET 엔드포인트가 있으면 추가
                .pathMatchers(HttpMethod.GET, "/api/v1/playlists/**").permitAll()
                .anyExchange().authenticated()
        }
        .exceptionHandling { handlers ->
            handlers.authenticationEntryPoint(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
        }
        .oauth2Login { login ->
            val success =
                RedirectServerAuthenticationSuccessHandler("http://localhost:1420/") // SPA 홈
            login.authenticationSuccessHandler(success)
        }
        .logout { logout ->
            logout.logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
        }
//        .csrf { csrf ->
//            csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
//        }
//        .csrf { csrf ->
//            // (1) 헤더 이름을 프런트와 맞추고 싶다면
//            val repo = CookieServerCsrfTokenRepository.withHttpOnlyFalse()
//            repo.setHeaderName("X-CSRF-TOKEN")
//            csrf.csrfTokenRepository(repo)
//
//            // (2) 특정 API는 CSRF 검사 제외
//            // csrf.ignoringRequestMatchers("/api/v1/user-play-session/**")
//
//            // (3) JWT/Token 기반이면 통째로 끄기
//            // csrf.disable()
//        }
        .csrf { csrf ->
            csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
            csrf.csrfTokenRequestHandler(ServerCsrfTokenRequestAttributeHandler())
            // ↑ 옛날 핸들러로 교체 → 평문 토큰 허용
        }
        .build()

    /** OIDC 로그아웃 후 리다이렉트 처리 */
    private fun oidcLogoutSuccessHandler(
        clientRegistrationRepository: ReactiveClientRegistrationRepository
    ): ServerLogoutSuccessHandler =
        OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository).apply {
            setPostLogoutRedirectUri("{baseUrl}")
        }

    /**
     * Spring Security 이슈(#5766) 대응용 WebFilter.
     * CSRF 토큰을 쿠키로 노출하기 전에 요청 컨텍스트에 저장.
     */
    @Bean
    fun csrfWebFilter(): WebFilter = WebFilter { exchange, chain ->
        exchange.response.beforeCommit {
            Mono.defer {
                val csrfToken: Mono<CsrfToken>? = exchange.getAttribute(CsrfToken::class.java.name)
                csrfToken?.then() ?: Mono.empty()
            }
        }
        chain.filter(exchange)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:1420")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("x-xsrf-token", "content-type", "authorization")
            allowCredentials = true                     // SESSION 쿠키 전송 허용
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)    // Gateway 자체 엔드포인트 전부
        }
    }
}
