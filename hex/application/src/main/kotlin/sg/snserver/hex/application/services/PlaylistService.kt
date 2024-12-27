package sg.snserver.hex.application.services

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import sg.snserver.hex.application.AlreadyExistsException
import sg.snserver.hex.application.NotExistsException
import sg.snserver.hex.application.inbound.GetPlaylistUseCase
import sg.snserver.hex.application.inbound.SaveYoutubeDataUseCase
import sg.snserver.hex.application.inbound.UpdateYoutubeDataUseCase
import sg.snserver.hex.application.outbound.GetPlaylistPort
import sg.snserver.hex.application.outbound.LoadYoutubeDataPort
import sg.snserver.hex.application.outbound.SavePlaylistPort
import sg.snserver.hex.application.outbound.UpdatePlaylistPort
import sg.snserver.hex.domain.entities.Playlist

@Service
class PlaylistService(
    private val loadYoutubeDataPort: LoadYoutubeDataPort,
    private val getPlaylistPort: GetPlaylistPort,
    private val savePlaylistPort: SavePlaylistPort,
    private val updatePlaylistPort: UpdatePlaylistPort,
): GetPlaylistUseCase, SaveYoutubeDataUseCase, UpdateYoutubeDataUseCase {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun savePlaylist(playlistId: String) {

        val playlist: Playlist? = getPlaylistPort.getPlaylist(playlistId)
        if (playlist != null) {
            throw AlreadyExistsException(playlistId)
        }

        val newPlaylist = loadYoutubeDataPort.loadYoutubeData(
            playlistId = playlistId,
        )

        log.debug(newPlaylist.toString())

        savePlaylistPort.savePlaylist(newPlaylist)
    }

    override fun updatePlaylist(playlistId: String) {

        // for decrease youtube api cost
        getPlaylistPort.getPlaylist(playlistId) ?: throw NotExistsException(playlistId)

        val updatedPlaylist = loadYoutubeDataPort.loadYoutubeData(
            playlistId = playlistId,
        )

        updatePlaylistPort.updatePlaylist(
            playlistId = playlistId,
            updatedPlaylist = updatedPlaylist,
        )
    }

    override fun getPlaylist(playlistId: String): Playlist {
        return getPlaylistPort.getPlaylist(playlistId) ?: throw NotExistsException(playlistId)
    }

    override fun getPlaylistBatch(pageable: Pageable): Page<Playlist> {
        return getPlaylistPort.getPlaylistBatch(pageable)
    }
}