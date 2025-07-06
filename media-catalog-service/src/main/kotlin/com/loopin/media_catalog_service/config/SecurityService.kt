package com.loopin.media_catalog_service.config

import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
class SecurityService {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun username(principal: JwtAuthenticationToken): String? {

        logger.info("Principal: ${principal.name}")

        logger.info("SecurityContextHolder: ${SecurityContextHolder.getContext()}")

        val name = SecurityContextHolder.getContext()?.authentication?.name
        if (name == "anonymousUser") {
            return null
        }
        return name
    }
    fun username(): String? {

//        logger.info("Principal: ${principal.name}")

        logger.info("SecurityContextHolder: ${SecurityContextHolder.getContext()}")

        val name = SecurityContextHolder.getContext()?.authentication?.name
        if (name == "anonymousUser") {
            return null
        }
        return name
    }
}