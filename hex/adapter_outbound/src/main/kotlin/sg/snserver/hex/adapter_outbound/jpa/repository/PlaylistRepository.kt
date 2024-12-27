package sg.snserver.hex.adapter_outbound.jpa.repository

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import sg.snserver.hex.adapter_outbound.jpa.entities.PlayItemEntity
import sg.snserver.hex.adapter_outbound.jpa.entities.PlaylistEntity
import sg.snserver.hex.adapter_outbound.jpa.entities.PlaylistItemManyEntity
import sg.snserver.hex.adapter_outbound.jpa.interfaces.*
import sg.snserver.hex.adapter_outbound.jpa.mapper.toEntity
import sg.snserver.hex.application.NotExistsException
import sg.snserver.hex.application.outbound.GetPlaylistPort
import sg.snserver.hex.application.outbound.SavePlaylistPort
import sg.snserver.hex.application.outbound.UpdatePlaylistPort
import sg.snserver.hex.domain.entities.Playlist

@Repository
class PlaylistRepository(
    private val playlistRepositoryJpa: PlaylistRepositoryJpa,
    private val playItemRepositoryJpa: PlayItemRepositoryJpa,
    private val playlistItemManyJpa: PlaylistItemManyJpa,
    private val localizedRepositoryJpa: LocalizedRepositoryJpa,
    private val contentDetailsRepositoryJpa: ContentDetailsRepositoryJpa,
    private val resourceRepositoryJpa: ResourceRepositoryJpa,
) : SavePlaylistPort, GetPlaylistPort, UpdatePlaylistPort {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun savePlaylist(playlist: Playlist) {
        if (playlist.items == null) {
            throw IllegalArgumentException("There is no items in the playlist when save.")
        }

        val newPlaylistEntity: PlaylistEntity

        with(playlist){
            val localizedEntity = localized.toEntity()
            localizedRepositoryJpa.save(localizedEntity)

            val contentDetailsEntity = contentDetails.toEntity()
            contentDetailsRepositoryJpa.save(contentDetailsEntity)

            newPlaylistEntity = PlaylistEntity(
                playlistId = playlistId,
                channelId = channelId,
                title = title,
                description = description,
                thumbnail = thumbnail,
                channelTitle = channelTitle,
                localized = localizedEntity,
                contentDetails = contentDetailsEntity,
                publishedAt = publishedAt,
                items = mutableListOf(),
                platformType = platformType.toEntity(),
            )
        }
        playlistRepositoryJpa.save(newPlaylistEntity)

        val playlistItemManyEntityList = mutableListOf<PlaylistItemManyEntity>()

        playlist.items!!.forEach { playItem ->
            val resourceEntity = playItem.resource.toEntity()
            resourceRepositoryJpa.save(resourceEntity)
            playlistItemManyEntityList.add(
                PlaylistItemManyEntity(
                    playlist = newPlaylistEntity,
                    playItem = PlayItemEntity(
                        videoId = playItem.videoId,
                        publishedAt = playItem.publishedAt,
                        channelId = playItem.channelId,
                        title = playItem.title,
                        description = playItem.description,
                        thumbnail = playItem.thumbnail,
                        channelTitle = playItem.channelTitle,
                        position = playItem.position,
                        resource = resourceEntity,
                        videoOwnerChannelId = playItem.videoOwnerChannelId,
                        videoOwnerChannelTitle = playItem.videoOwnerChannelTitle,
                        startSeconds = playItem.startSeconds,
                        isDeleted = playItem.isDeleted,
                        platformType = playItem.platformType.toEntity(),
                    ),
                )
            )
        }

        log.debug(playlistItemManyEntityList.toString())

        playlistItemManyJpa.saveAll(playlistItemManyEntityList)

        newPlaylistEntity.items = playlistItemManyEntityList

        playlistRepositoryJpa.save(newPlaylistEntity)
    }

    override fun getPlaylist(playlistId: String): Playlist? {
        return playlistRepositoryJpa.findByPlaylistId(playlistId)?.toDomain()
    }

    override fun getPlaylistBatch(pageable: Pageable): Page<Playlist> {
        return playlistRepositoryJpa.findAll(pageable).map { it.toDomain() }
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
//            items = updatedPlaylist.items!!.map { it.toEntity() }.toMutableList()
        }

        // standalone mongodb is not support transaction
        // save it directly.
        val updatedEntity = playlistRepositoryJpa.save(basePlaylistEntity)

        return updatedEntity.toDomain()
    }
}