package sg.snserver.hex.application.services

import org.springframework.stereotype.Service
import sg.snserver.hex.application.outbound.GetUserPlaysPort

@Service
class UserPlaysService(
    private val getUserPlaysPort: GetUserPlaysPort,
) {
}