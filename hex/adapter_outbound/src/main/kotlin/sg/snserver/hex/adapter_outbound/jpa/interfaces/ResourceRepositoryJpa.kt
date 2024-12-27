package sg.snserver.hex.adapter_outbound.jpa.interfaces

import org.springframework.data.jpa.repository.JpaRepository
import sg.snserver.hex.adapter_outbound.jpa.entities.ResourceEntity
import java.util.UUID

interface ResourceRepositoryJpa: JpaRepository<ResourceEntity, UUID> {
}