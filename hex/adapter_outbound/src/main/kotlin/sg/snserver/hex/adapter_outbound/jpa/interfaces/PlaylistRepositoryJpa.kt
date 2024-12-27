package sg.snserver.hex.adapter_outbound.jpa.interfaces

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import sg.snserver.hex.adapter_outbound.jpa.dto.PlaylistBatchDTO
import sg.snserver.hex.adapter_outbound.jpa.entities.PlaylistEntity
import java.util.*

interface PlaylistRepositoryJpa : JpaRepository<PlaylistEntity, String> {
    fun findByPlaylistId(playlistId: String): PlaylistEntity?

//    @Query(
//        """
//        SELECT new sg.snserver.hex.domain.dto.PlaylistDTO(
//            p.playlistId,
//            p.channelId,
//            p.title,
//            p.description,
//            p.thumbnail,
//            p.channelTitle,
//            p.localized,
//            p.contentDetails,
//            p.publishedAt
//        )
//        FROM PlaylistEntity p
//    """
//    )
//    fun findAllWithoutItems(page: Pageable): Page<PlaylistBatchDTO>
}