package sg.snserver.hex.adapter_outbound.jpa.interfaces

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import sg.snserver.hex.adapter_outbound.jpa.entities.CurrentPlayEntity
import sg.snserver.hex.adapter_outbound.jpa.entities.PlaylistEntity
import sg.snserver.hex.adapter_outbound.jpa.entities.UserPlaysEntity
import java.util.UUID

interface CurrentPlayRepositoryJpa: JpaRepository<CurrentPlayEntity, UUID> {
    fun findByUserPlaysAndPlaylist(
        userPlaysEntity: UserPlaysEntity,
        playlistEntity: PlaylistEntity,
    ): CurrentPlayEntity?

//    @EntityGraph(attributePaths = ["playlist"])
//    @Query("SELECT c FROM CurrentPlayEntity c",
//        countQuery = "select count(c) from CurrentPlayEntity c",
//    )
//    fun findAllByUserPlaysBatch(pageable: Pageable, userPlaysEntity: UserPlaysEntity): Page<CurrentPlayEntity>
    @EntityGraph(attributePaths = ["playlist"])
    @Query("SELECT c FROM CurrentPlayEntity c",
        countQuery = "select count(c) from CurrentPlayEntity c",
    )
    fun findAllByUserPlaysBatch(userPlaysEntity: UserPlaysEntity): List<CurrentPlayEntity>
}