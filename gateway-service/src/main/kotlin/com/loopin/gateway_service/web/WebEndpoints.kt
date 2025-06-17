package com.loopin.gateway_service.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class WebEndpoints {

    @Bean
    fun routerFunction(): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .GET("/playback-fallback") {
                ServerResponse.ok()
                    .body(BodyInserters.fromValue(""))
            }
            .GET("/media-catalog-fallback") {
                ServerResponse.ok()
                    .body(BodyInserters.fromValue(""))
            }
            .POST("/playback-fallback") {
                ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
            }
            .POST("/media-catalog-fallback") {
                ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build()
            }
            .build()
}