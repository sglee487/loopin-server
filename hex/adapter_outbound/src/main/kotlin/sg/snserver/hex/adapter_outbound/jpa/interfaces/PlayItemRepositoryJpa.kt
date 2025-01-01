package sg.snserver.hex.adapter_outbound.jpa.interfaces

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import sg.snserver.hex.adapter_outbound.jpa.entities.PlayItemEntity

interface PlayItemRepositoryJpa: JpaRepository<PlayItemEntity, String> {
    @EntityGraph(attributePaths = ["resource"])
    @Query("SELECT p FROM PlayItemEntity p WHERE p.playItemId IN :ids")
    fun findAllByPlayItemIds(ids: List<String>): List<PlayItemEntity>
}