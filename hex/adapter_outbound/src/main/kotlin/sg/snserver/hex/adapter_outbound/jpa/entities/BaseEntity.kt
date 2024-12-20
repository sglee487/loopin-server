package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    var createdAt: Instant = Instant.now()

    @LastModifiedDate
    var updatedAt: Instant = Instant.now()
}

class AuditEntityListener {
    @PrePersist
    fun prePersist(target: BaseEntity) {
        val now = Instant.now()
        target.createdAt = now
        target.updatedAt = now
    }

    @PreUpdate
    fun preUpdate(target: BaseEntity) {
        target.updatedAt = Instant.now()
    }
}