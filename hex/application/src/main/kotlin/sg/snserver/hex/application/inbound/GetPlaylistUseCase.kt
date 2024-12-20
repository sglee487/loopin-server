package sg.snserver.hex.application.inbound

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sg.snserver.hex.domain.entities.Playlist

interface GetPlaylistUseCase {
    fun getPlaylist(playlistId: String): Playlist
    fun getPlaylistBatch(
        pageable: Pageable,
    ): Page<Playlist>
}