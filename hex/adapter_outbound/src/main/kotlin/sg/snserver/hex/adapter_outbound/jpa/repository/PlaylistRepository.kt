package sg.snserver.hex.adapter_outbound.jpa.repository

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import sg.snserver.hex.adapter_outbound.jpa.interfaces.PlaylistRepositoryJpa
import sg.snserver.hex.adapter_outbound.jpa.mapper.toEntity
import sg.snserver.hex.application.NotExistsException
import sg.snserver.hex.application.outbound.GetPlaylistPort
import sg.snserver.hex.application.outbound.SavePlaylistPort
import sg.snserver.hex.application.outbound.UpdatePlaylistPort
import sg.snserver.hex.domain.entities.Playlist

@Repository
class PlaylistRepository(
    private val playlistRepositoryJpa: PlaylistRepositoryJpa,
) : SavePlaylistPort, GetPlaylistPort, UpdatePlaylistPort {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun savePlaylist(playlist: Playlist) {
        playlistRepositoryJpa.save(playlist.toEntity())
    }

    override fun getPlaylist(playlistId: String): Playlist? {
        return playlistRepositoryJpa.findByPlaylistId(playlistId)?.toDomain()
    }

    override fun getPlaylistBatch(pageable: Pageable): Page<Playlist> {
        return playlistRepositoryJpa.findAllWithoutItems(pageable).map { it.toDomain() }
    }

    override fun updatePlaylist(playlistId: String, updatedPlaylist: Playlist): Playlist {

        val basePlaylistEntity = playlistRepositoryJpa.findByPlaylistId(
            playlistId = playlistId
        ) ?: throw NotExistsException("playlist: $playlistId not exists")

        log.debug(basePlaylistEntity.toString())
        log.debug(updatedPlaylist.toString())

        with(basePlaylistEntity) {
            title = updatedPlaylist.title
            description = updatedPlaylist.description
            thumbnail = updatedPlaylist.thumbnail
            channelTitle = updatedPlaylist.channelTitle
            localized = updatedPlaylist.localized.toEntity()
            contentDetails = updatedPlaylist.contentDetails.toEntity()
            items = updatedPlaylist.items!!.map { it.toEntity() }.toMutableList()
        }

        // standalone mongodb is not support transaction
        // save it directly.
        val updatedEntity = playlistRepositoryJpa.save(basePlaylistEntity)

        return updatedEntity.toDomain()
    }
}