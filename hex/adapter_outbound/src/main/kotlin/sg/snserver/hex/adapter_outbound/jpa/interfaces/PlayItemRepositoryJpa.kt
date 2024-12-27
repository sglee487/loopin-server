package sg.snserver.hex.adapter_outbound.jpa.interfaces

import org.springframework.data.jpa.repository.JpaRepository
import sg.snserver.hex.adapter_outbound.jpa.entities.PlayItemEntity

interface PlayItemRepositoryJpa: JpaRepository<PlayItemEntity, String> {
}