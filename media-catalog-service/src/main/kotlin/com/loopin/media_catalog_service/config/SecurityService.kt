package com.loopin.media_catalog_service.config

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

//@Component("securityService")     // SpEL에서 @securityService 로 참조
//class SecurityService {
//
//    /** 로그인돼 있으면 username, 아니면 null */
//    fun username(): String? {
//        // WebFlux에선 reactive 컨텍스트 우선
//        val auth = ReactiveSecurityContextHolder.getContext()
//            .map { it.authentication }
//            .switchIfEmpty(Mono.empty())
//            .block()                 // block() 해도 아주 짧은 동기 호출
//
//        return when {
//            auth == null               -> null
//            !auth.isAuthenticated      -> null
//            auth.name == "anonymousUser" -> null
//            else                       -> auth.name   // ex) 8a3be86c-…
//        }
//    }
//}

//@Service
//class SecurityService {
//
//    /**
//     * 로그인한 경우 → username
//     * 익명(비로그인)·NULL → null 반환
//     */
//    fun username(): String? =
//        ReactiveSecurityContextHolder.getContext()
//            .map { ctx ->
//                ctx.authentication
//                    ?.takeIf { it.isAuthenticated && it.name != "anonymousUser" }
//                    ?.name
//            }
//            .blockOptional()   // 1-회 블로킹 (SpEL 호출 직후 끝) → 실질적인 영향 없음
//            .orElse(null)
//}

//@Service
//class SecurityService {
//    private val logger = org.slf4j.LoggerFactory.getLogger(javaClass)
//
//    fun username(): String? {
//        logger.info("authentication: ${SecurityContextHolder.getContext().authentication}")
//        val name = SecurityContextHolder.getContext().authentication.name
//        logger.info("username: $name")
//        if (name == "anonymousUser") {
//            return null
//        }
//        return name
//    }
//}

@Service("securityService")
class SecurityService {
    fun username(): String? =
        SecurityContextHolder.getContext().authentication
            ?.takeIf { it.isAuthenticated && it.name != "anonymousUser" }
            ?.name
}