package sg.snserver.hex.domain.entities

import java.util.UUID

data class ContentDetails(
    val playlistId: String,

    var itemCount: Long,
): Base()
