package sg.snserver.hex.application.outbound

import sg.snserver.hex.domain.entities.PlayItem
import java.util.*

interface SaveCurrentPlayPort {
    fun saveCurrentPlay(
        userId: UUID,
        playlistId: String,
        nowPlayingItem: PlayItem,
        prevItemIdList: List<String>,
        nextItemIdList: List<String>
    )
}