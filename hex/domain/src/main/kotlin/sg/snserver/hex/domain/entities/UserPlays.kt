package sg.snserver.hex.domain.entities

import java.util.UUID

data class UserPlays(
    val id: UUID = UUID.randomUUID(),

    val userId: UUID,
    val currentPlays: MutableList<CurrentPlay>,
): Base()
