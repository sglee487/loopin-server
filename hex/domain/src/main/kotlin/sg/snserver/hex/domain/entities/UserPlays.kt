package sg.snserver.hex.domain.entities

import java.time.Instant
import java.util.UUID

data class UserPlays(
    override val id: UUID, // this id will be user id

    val plays: Plays,
): Base(id)
