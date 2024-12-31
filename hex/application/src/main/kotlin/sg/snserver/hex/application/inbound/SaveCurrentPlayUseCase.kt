package sg.snserver.hex.application.inbound

import sg.snserver.hex.domain.entities.PlayItem
import java.util.*

interface SaveCurrentPlayUseCase {
    fun saveCurrentPlay(
        userId: UUID,
        playlistId: String,
        nowPlayingItem: PlayItem,
        prevItemIdList: List<String>,
        nextItemIdList: List<String>,
    )
}