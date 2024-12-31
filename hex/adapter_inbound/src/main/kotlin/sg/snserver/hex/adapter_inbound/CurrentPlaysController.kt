package sg.snserver.hex.adapter_inbound

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import sg.snserver.hex.adapter_inbound.dto.*
import sg.snserver.hex.application.inbound.GetCurrentPlaysUseCase
import sg.snserver.hex.application.inbound.SaveCurrentPlayUseCase
import java.util.*

@RestController
@RequestMapping(
    "/api/v1/current-plays",
    consumes = [],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class CurrentPlaysController(
    private val saveCurrentPlayUseCase: SaveCurrentPlayUseCase,
    private val getCurrentPlaysUseCase: GetCurrentPlaysUseCase,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun saveCurrentPlay(
//        principal: Principal,
        @RequestBody request: SaveCurrentPlayRequestDTO
    ): ApiResponseDTO.Success<Unit> {
        saveCurrentPlayUseCase.saveCurrentPlay(
            userId = UUID.fromString("e120807e-32b7-4284-914d-3638f5bf3c08"),
            playlistId = request.playlistId,
            nowPlayingItem = request.nowPlayingItem.toDomain(),
            prevItemIdList = request.prevItemIdList,
            nextItemIdList = request.nextItemIdList,
        )

        return ApiResponseDTO.emptySuccess(
            message = "save current play ${request.playlistId}"
        )
    }

    @GetMapping
    fun getCurrentPlay(
        @RequestParam playlistId: String,
    ): ApiResponseDTO.Success<GetCurrentPlayRequestDTO> {
        val currentPlay = getCurrentPlaysUseCase.getCurrentPlayUseCase(
            userId = UUID.fromString("e120807e-32b7-4284-914d-3638f5bf3c08"),
            playlistId = playlistId,
        )
        return ApiResponseDTO.Success(
            message = "get current play success",
            data = currentPlay.toResponseDTO()
        )
    }

    @GetMapping("/batch")
    fun getCurrentPlayBatch(
        pageable: Pageable,
    ): ApiResponseDTO.Success<GetCurrentPlayBatchResponseDTO> {
        val currentPlayPage = getCurrentPlaysUseCase.getCurrentPlayBatchUseCase(
            userId = UUID.fromString("e120807e-32b7-4284-914d-3638f5bf3c08"),
            pageable = pageable,
        )
        return ApiResponseDTO.Success(
            message = "get current play success",
            data = GetCurrentPlayBatchResponseDTO(
                currentPlays = currentPlayPage.map { it.toResponseDTO() }
            )
        )
    }
}