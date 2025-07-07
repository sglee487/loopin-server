package com.loopin.media_catalog_service.config

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.security.Principal

@Component
@Order(329)
class UsernameHeaderFilter : WebFilter {

    private val logger = org.slf4j.LoggerFactory.getLogger(javaClass)

    companion object {
        const val HEADER = "X-Username"
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        exchange.getPrincipal<Principal>()
            .map {
                logger.debug("User {} accessed {}", it.name, exchange.request.path)
                it.name
            }
            .defaultIfEmpty("anonymous")
            .flatMap { username ->

                logger.info("Injecting username $username into request header")

                val mutatedReq = exchange.request.mutate()
                    .header(HEADER, username)
                    .build()

                val mutatedEx = exchange.mutate()
                    .request(mutatedReq)
                    .build()

                chain.filter(mutatedEx)
            }
}