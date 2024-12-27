package sg.snserver.hex.domain.entities

import java.util.*

data class PlaylistQueues(
    val id: UUID = UUID.randomUUID(),

    var prev: MutableList<PlayItem>,
    var next: MutableList<PlayItem>,
): Base()
