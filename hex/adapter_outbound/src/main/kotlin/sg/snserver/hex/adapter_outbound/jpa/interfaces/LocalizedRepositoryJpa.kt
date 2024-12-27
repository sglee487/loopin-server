package sg.snserver.hex.adapter_outbound.jpa.interfaces

import org.springframework.data.jpa.repository.JpaRepository
import sg.snserver.hex.adapter_outbound.jpa.entities.LocalizedEntity

interface LocalizedRepositoryJpa: JpaRepository<LocalizedEntity, String> {
}