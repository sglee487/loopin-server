package sg.snserver.hex.domain.entities

import java.time.Instant
import java.util.*

abstract class Base(
    open val id: UUID = UUID.randomUUID(),

    open val createdAt: Instant? = null,
    open var updatedAt: Instant? = null,
)
