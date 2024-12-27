package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.*
import sg.snserver.hex.domain.entities.PlayItem
import sg.snserver.hex.domain.entities.Playlist
import java.util.UUID

@Entity
@Table(name = "sn_play_list_item_many")
data class PlaylistItemManyEntity(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sn_playlist_id", nullable = false)
    val playlist: PlaylistEntity,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val playItem: PlayItemEntity,

    ): BaseEntity() {
    fun toDomain(): PlayItem {
        return playItem.toDomain()
    }
}
