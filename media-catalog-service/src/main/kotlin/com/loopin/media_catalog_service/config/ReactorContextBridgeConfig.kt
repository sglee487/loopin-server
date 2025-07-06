package com.loopin.media_catalog_service.config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class ReactorContextBridgeConfig {

    @PostConstruct
    fun enableBridge() {
        reactor.core.publisher.Hooks.enableAutomaticContextPropagation()
    }
}