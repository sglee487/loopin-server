package sg.snserver.hex.application.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import sg.snserver.hex.application.inbound.GetCurrentPlaysUseCase
import sg.snserver.hex.application.inbound.SaveCurrentPlayUseCase
import sg.snserver.hex.application.outbound.GetCurrentPlayPort
import sg.snserver.hex.application.outbound.SaveCurrentPlayPort
import sg.snserver.hex.domain.entities.CurrentPlay
import sg.snserver.hex.domain.entities.PlayItem
import java.util.*

@Service
class CurrentPlaysService(
    private val saveCurrentPlayPort: SaveCurrentPlayPort,
    private val getCurrentPlayPort: GetCurrentPlayPort,
): SaveCurrentPlayUseCase, GetCurrentPlaysUseCase {

    override fun saveCurrentPlay(
        userId: UUID,
        playlistId: String,
        nowPlayingItem: PlayItem,
        prevItemIdList: List<String>,
        nextItemIdList: List<String>,
    ) {
        saveCurrentPlayPort.saveCurrentPlay(
            userId = userId,
            playlistId = playlistId,
            nowPlayingItem = nowPlayingItem,
            prevItemIdList = prevItemIdList,
            nextItemIdList = nextItemIdList,
        )
    }

    override fun getCurrentPlayUseCase(userId: UUID, playlistId: String): CurrentPlay {
        return getCurrentPlayPort.getCurrentPlay(
            userId = userId,
            playlistId = playlistId,
        )
    }

    override fun getCurrentPlayBatchUseCase(userId: UUID, pageable: Pageable): Page<CurrentPlay> {
        return getCurrentPlayPort.getCurrentPlays(userId, pageable)
    }
}