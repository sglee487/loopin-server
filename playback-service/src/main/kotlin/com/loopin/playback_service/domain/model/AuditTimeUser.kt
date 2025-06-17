package com.loopin.playback_service.domain.model

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy

interface AuditTimeUser: AuditTime {
    @get:CreatedBy
    val createdBy: String?

    @get:LastModifiedBy
    var updatedBy: String?
}
