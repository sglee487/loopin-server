package sg.snserver.hex.application.outbound

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sg.snserver.hex.domain.entities.CurrentPlay
import java.util.*

interface GetCurrentPlayPort {
    fun getCurrentPlay(
        userId: UUID,
        playlistId: String,
    ): CurrentPlay
    fun getCurrentPlays(
        userId: UUID,
        pageable: Pageable,
    ): Page<CurrentPlay>
}