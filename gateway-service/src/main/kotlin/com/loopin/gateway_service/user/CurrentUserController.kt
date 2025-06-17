package com.loopin.gateway_service.user

import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * 현재 로그인한 사용자 프로필 조회 컨트롤러.
 */
@RestController
@RequestMapping("/api/v1")   // 버전 프리픽스
class CurrentUserController {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/user")
    fun getCurrentUser(@AuthenticationPrincipal oidcUser: OidcUser): Mono<User> {
        log.info("Fetching information about the currently authenticated user")

        val currentUser = User(
            username  = oidcUser.preferredUsername,
            firstName = oidcUser.givenName ?: "",
            lastName  = oidcUser.familyName ?: "",
            roles     = oidcUser.getClaimAsStringList("roles") ?: emptyList()
        )

        return Mono.just(currentUser)
    }
}