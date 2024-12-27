package sg.snserver.hex.adapter_outbound.jpa.interfaces

import org.springframework.data.jpa.repository.JpaRepository
import sg.snserver.hex.adapter_outbound.jpa.entities.PlaylistItemManyEntity
import java.util.UUID

interface PlaylistItemManyJpa: JpaRepository<PlaylistItemManyEntity, UUID> {
}