package sg.snserver.hex.application.outbound

import sg.snserver.hex.domain.entities.Playlist

interface LoadYoutubeDataPort {
    fun loadYoutubeData(playlistId: String): Playlist
}