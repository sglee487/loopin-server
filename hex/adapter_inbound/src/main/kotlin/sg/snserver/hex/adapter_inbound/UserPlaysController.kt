//package sg.snserver.hex.adapter_inbound
//
//import org.slf4j.LoggerFactory
//import org.springframework.http.MediaType
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//import sg.snserver.hex.adapter_inbound.dto.ApiResponseDTO
//import sg.snserver.hex.adapter_inbound.dto.GetNewPlayItemResponseDTO
//import sg.snserver.hex.application.inbound.GetUserCurrentPlaysUseCase
//import java.security.Principal
//import java.util.*
//
//@RestController
//@RequestMapping(
//    "/api/v1/user-plays",
//    consumes = [],
//    produces = [MediaType.APPLICATION_JSON_VALUE],
//)
//class UserPlaysController(
//    private val getUserCurrentPlaysUseCase: GetUserCurrentPlaysUseCase,
//) {
//
//    private val log = LoggerFactory.getLogger(javaClass)
//
//    @GetMapping("/current-plays")
//    fun getUserCurrentPlays(
//        principal: Principal
////    ): ApiResponseDTO.Success<Map<String, GetNewPlayItemResponseDTO>> {
//    ) {
//        val userCurrentPlays = getUserCurrentPlaysUseCase.getUserCurrentPlays(
//            userId = UUID.fromString(principal.name) ?: throw IllegalArgumentException("user id is not exist")
//        )
//    }
//}