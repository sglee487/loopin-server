package sg.snserver.hex.domain.entities

import java.time.Instant
import java.util.*

abstract class Base(
    open val createdAt: Instant = Instant.now(),
    open var updatedAt: Instant = Instant.now(),
)
