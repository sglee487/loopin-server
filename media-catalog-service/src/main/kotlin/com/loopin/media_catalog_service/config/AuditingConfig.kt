package com.loopin.media_catalog_service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.oauth2.jwt.Jwt
import reactor.core.publisher.Mono

@Configuration
@EnableR2dbcAuditing
class AuditingConfig {

    @Bean
    fun auditorAware(): ReactiveAuditorAware<String> =
        ReactiveAuditorAware {
            ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map { auth ->
                    when (val principal = auth.principal) {
                        is Jwt -> principal.getClaimAsString("preferred_username")
                            ?: principal.getClaimAsString("sub")
                            ?: auth.name       // fallback
                        else -> auth.name    // e.g. opaque token
                    }
                }
                .switchIfEmpty(Mono.empty())   // 인증 없으면 NULL 반환
        }

//    @Bean
//    fun auditorAware(): ReactiveAuditorAware<String> {
//        return ReactiveAuditorAware {
//            ReactiveSecurityContextHolder.getContext()
//                .map { obj: SecurityContext -> obj.authentication }
//                .filter { obj: Authentication -> obj.isAuthenticated }
//                .map { obj: Authentication -> obj.name }
//        }
//    }

}