package sg.snserver.hex.application.services

import org.springframework.stereotype.Service
import sg.snserver.hex.application.inbound.GetUserCurrentPlaysUseCase
import sg.snserver.hex.domain.entities.CurrentPlay
import java.util.*

@Service
class CurrentPlaysService: GetUserCurrentPlaysUseCase {
    override fun getUserCurrentPlays(userId: UUID): List<Map<String, CurrentPlay>> {
        TODO("Not yet implemented")
    }
}