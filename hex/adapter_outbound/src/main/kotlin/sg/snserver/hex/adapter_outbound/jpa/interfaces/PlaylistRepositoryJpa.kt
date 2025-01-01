package sg.snserver.hex.adapter_outbound.jpa.interfaces

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import sg.snserver.hex.adapter_outbound.jpa.entities.PlaylistEntity

interface PlaylistRepositoryJpa : JpaRepository<PlaylistEntity, String> {
    @EntityGraph(attributePaths = ["localized", "contentDetails"])
    fun findByPlaylistId(playlistId: String): PlaylistEntity?

    @EntityGraph(attributePaths = ["localized", "contentDetails"])
    @Query("SELECT p FROM PlaylistEntity p",
        countQuery = "select count(p) from PlaylistEntity p",
    )
    fun findAllWithoutItems(page: Pageable): Page<PlaylistEntity>
}