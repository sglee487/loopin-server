//package com.loopin.media_catalog_service.config
//
//import jakarta.annotation.PostConstruct
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.core.context.SecurityContextHolder
//
//
//@Configuration
//class ReactiveContextBridgeConfig {
//
//    @PostConstruct
//    fun enableReactiveStrategy() {
//        // Spring Security 6.3+ 부터 존재하는 전략 구현체의 FQCN
//        SecurityContextHolder.setStrategyName(
//            "org.springframework.security.core.context.ReactorContextSecurityContextHolderStrategy"
//        )
//    }
//}