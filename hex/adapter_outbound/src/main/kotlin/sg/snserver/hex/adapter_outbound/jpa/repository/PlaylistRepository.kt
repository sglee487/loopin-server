package sg.snserver.hex.adapter_outbound.jpa.repository

import org.springframework.stereotype.Repository
import sg.snserver.hex.adapter_outbound.jpa.interfaces.PlaylistRepositoryJpa
import sg.snserver.hex.adapter_outbound.jpa.mapper.toEntity
import sg.snserver.hex.application.outbound.SavePlaylistPort
import sg.snserver.hex.domain.entities.Playlist

@Repository
class PlaylistRepository(
    private val playlistRepositoryJpa: PlaylistRepositoryJpa,
): SavePlaylistPort {
    override fun savePlaylist(playlist: Playlist) {
        playlistRepositoryJpa.save(playlist.toEntity())
    }
}