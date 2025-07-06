package com.loopin.media_catalog_service.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories(
    basePackages = ["com.loopin.media_catalog_service.domain.repository"]
)
class R2dbcConfig