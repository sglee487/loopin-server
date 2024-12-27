//package sg.snserver.hex.adapter_inbound
//
//import org.springframework.http.MediaType
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//import sg.snserver.hex.adapter_inbound.dto.ApiResponseDTO
//import sg.snserver.hex.adapter_inbound.dto.CurrentPlayResponseDTO
//
//@RestController
//@RequestMapping(
//    "/api/v1/current-plays",
//    produces = [MediaType.APPLICATION_JSON_VALUE],
//)
//class CurrentPlaysController(
//    private val getCurrentPlaysUseCase: GetCurrentPlaysUseCase,
//) {
//
//    @GetMapping
//    fun getCurrentPlays(): ApiResponseDTO.Success<List<Map<String, CurrentPlayResponseDTO>>> {
//
//
//    }
//}