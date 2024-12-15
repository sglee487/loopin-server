package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.*
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditEntityListener::class)
abstract class BaseEntity(

    @Column(updatable = false)
    open var createdAt: Instant? = null,

    @Column()
    open var updatedAt: Instant? = null
)

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