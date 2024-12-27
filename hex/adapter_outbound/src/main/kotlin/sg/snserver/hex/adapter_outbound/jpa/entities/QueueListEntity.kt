package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "sn_queue_list")
data class QueueListEntity(
    @Id
    val id: UUID,

    var playItemOrder: Int,

    @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    var playItem: PlayItemEntity,

    ): BaseEntity()
