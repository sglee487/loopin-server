package sg.snserver.hex.application.outbound

import sg.snserver.hex.domain.entities.Playlist

interface SavePlaylistPort {
    fun savePlaylist(playlist: Playlist)
}