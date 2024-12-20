package sg.snserver.hex.application.outbound

import sg.snserver.hex.domain.entities.Playlist

interface UpdatePlaylistPort {
    fun updatePlaylist(
        playlistId: String,
        updatedPlaylist: Playlist,
    ): Playlist
}