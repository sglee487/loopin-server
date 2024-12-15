package sg.snserver.hex.application.services

import org.springframework.stereotype.Service
import sg.snserver.hex.application.inbound.GetYoutubeDataUseCase
import sg.snserver.hex.application.outbound.LoadYoutubeDataPort
import sg.snserver.hex.application.outbound.SavePlaylistPort

@Service
class YoutubeDataService(
    private val loadYoutubeDataPort: LoadYoutubeDataPort,
    private val savePlaylistPort: SavePlaylistPort,
): GetYoutubeDataUseCase {
    override fun getPlaylist(playlistId: String, refresh: Boolean) {
        val playlist = loadYoutubeDataPort.loadYoutubeData(
            playlistId = playlistId,
            refresh = refresh,
        )

        savePlaylistPort.savePlaylist(playlist)
    }
}