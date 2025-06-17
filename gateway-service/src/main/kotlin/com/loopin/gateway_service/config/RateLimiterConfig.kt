package com.loopin.gateway_service.config

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.Principal

@Configuration
class RateLimiterConfig {

    /**
     * 요청한 사용자의 Principal 이름(없으면 "anonymous")을
     * RedisRateLimiter 등에서 사용되는 key 로 사용.
     */
    @Bean
    fun keyResolver(): KeyResolver =
        KeyResolver { exchange ->
            exchange.getPrincipal<Principal>()
                .map(Principal::getName)
                .defaultIfEmpty("anonymous")
        }
}