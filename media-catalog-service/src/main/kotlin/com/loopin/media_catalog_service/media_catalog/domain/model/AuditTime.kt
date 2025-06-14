package com.loopin.media_catalog_service.media_catalog.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

interface AuditTime {
    @get:CreatedDate
    val createdAt: Instant?

    @get:LastModifiedDate
    var updatedAt: Instant?
}