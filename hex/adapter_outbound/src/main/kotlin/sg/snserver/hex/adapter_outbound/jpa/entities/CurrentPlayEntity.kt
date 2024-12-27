package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "sn_current_play")
data class CurrentPlayEntity(
    @Id
    val id: UUID,

    @JoinColumn
    @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    val userPlays: UserPlaysEntity,

    @JoinColumn
    @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    val nowPlayingItem: PlayItemEntity,

    @JoinColumn
    @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    val playlist: PlaylistEntity,

    @OneToMany(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST]
    )
    val prev: MutableList<QueueListEntity>,

    @OneToMany(
        cascade = [CascadeType.MERGE, CascadeType.PERSIST]
    )
    val next: MutableList<QueueListEntity>,

): BaseEntity()