package sg.snserver.hex.adapter_outbound.jpa.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "sn_user_plays")
data class UserPlaysEntity(
    @Id
    val id: UUID,

    @JoinColumn
    @OneToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    val currentPlays: MutableList<CurrentPlayEntity>,
): BaseEntity()
