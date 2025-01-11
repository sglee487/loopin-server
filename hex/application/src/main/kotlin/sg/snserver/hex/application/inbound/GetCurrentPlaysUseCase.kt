package sg.snserver.hex.application.inbound

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sg.snserver.hex.domain.entities.CurrentPlay
import java.util.*

interface GetCurrentPlaysUseCase {
    fun getCurrentPlayUseCase(userId: UUID, playlistId: String): CurrentPlay
//    fun getCurrentPlayBatchUseCase(
//        userId: UUID,
////        pageable: Pageable,
//    ): Page<CurrentPlay>
    fun getCurrentPlayBatchUseCase(
        userId: UUID,
    ): List<CurrentPlay>
}