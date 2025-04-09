package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.*

@Entity
@Table(name = "sn_playlist_items")
class PlaylistItemEntity(
    @Id @GeneratedValue
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sn_playlist_id")
    val playlist: PlaylistEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sn_play_item_video_id")
    val playItem: PlayItemEntity,
) {

}