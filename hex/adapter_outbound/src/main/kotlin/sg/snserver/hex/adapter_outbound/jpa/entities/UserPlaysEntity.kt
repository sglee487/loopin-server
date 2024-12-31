package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "sn_user_plays")
data class UserPlaysEntity(
    @Id
    val id: UUID = UUID.randomUUID(),

    val userId: UUID,

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "userPlays", cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    val currentPlays: MutableList<CurrentPlayEntity>,
): BaseEntity()
