package sg.snserver.hex.application.inbound

import sg.snserver.hex.domain.entities.UserPlays
import java.util.UUID

interface GetUserPlaysUseCase {
    fun getUserPlays(userId: UUID): UserPlays?
}