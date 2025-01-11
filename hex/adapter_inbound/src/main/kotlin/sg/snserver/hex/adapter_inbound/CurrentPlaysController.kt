package sg.snserver.hex.adapter_inbound

import org.slf4j.LoggerFactory
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
@CrossOrigin(origins = ["http://localhost:14200", "http://localhost:6006"])
class CurrentPlaysController(
    private val saveCurrentPlayUseCase: SaveCurrentPlayUseCase,
    private val getCurrentPlaysUseCase: GetCurrentPlaysUseCase,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping(
        "/{playlistId}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun saveCurrentPlay(
//        principal: Principal,
        @PathVariable playlistId: String,
        @RequestBody request: SaveCurrentPlayRequestDTO
    ): ApiResponseDTO.Success<Unit> {
        saveCurrentPlayUseCase.saveCurrentPlay(
            userId = UUID.fromString("e120807e-32b7-4284-914d-3638f5bf3c08"),
            playlistId = playlistId,
            nowPlayingItem = request.nowPlayingItem?.toDomain(),
            prevItemIdList = request.prevItemIdList,
            nextItemIdList = request.nextItemIdList,
            startSeconds = request.startSeconds,
        )

        return ApiResponseDTO.emptySuccess(
            message = "save current play ${playlistId}"
        )
    }

    @PostMapping(
        "/{playlistId}/start-seconds",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun saveCurrentPlayStartSeconds(
//        principal: Principal,
        @PathVariable playlistId: String,
        @RequestBody request: SaveCurrentPlayStartSecondsRequestDTO,
    ): ApiResponseDTO.Success<Unit> {
        saveCurrentPlayUseCase.setStartSeconds(
            userId = UUID.fromString("e120807e-32b7-4284-914d-3638f5bf3c08"),
            playlistId = playlistId,
            startSeconds = request.startSeconds,
        )

        return ApiResponseDTO.emptySuccess(
            message = "set startSeconds in playlistId $playlistId"
        )
    }

    //    @GetMapping
//    fun getCurrentPlays(
//        pageable: Pageable,
//    ): ApiResponseDTO.Success<GetCurrentPlayBatchResponseDTO> {
//        val currentPlayPage = getCurrentPlaysUseCase.getCurrentPlayBatchUseCase(
//            userId = UUID.fromString("e120807e-32b7-4284-914d-3638f5bf3c08"),
//            pageable = pageable,
//        )
//        return ApiResponseDTO.Success(
//            message = "get current play success",
//            data = GetCurrentPlayBatchResponseDTO(
//                currentPlays = currentPlayPage.map { it.toResponseDTO() }
//            )
//        )
//    }

    @GetMapping
    fun getCurrentPlays(
    ): ApiResponseDTO.Success<GetCurrentPlayBatchResponseDTO> {
        val currentPlays = getCurrentPlaysUseCase.getCurrentPlayBatchUseCase(
            userId = UUID.fromString("e120807e-32b7-4284-914d-3638f5bf3c08"),
        )

        val currentPlayMap = currentPlays.associateBy(
            keySelector = { it.playlist.playlistId },
            valueTransform = { it.toBatchSingleResponseDTO() }
        )

        return ApiResponseDTO.Success(
            message = "get current play success",
            data = GetCurrentPlayBatchResponseDTO(
                currentPlayMap = currentPlayMap
            )
        )
    }

    @GetMapping("/{playlistId}")
    fun getCurrentPlay(
        @PathVariable playlistId: String,
    ): ApiResponseDTO.Success<CurrentPlayResponseDTO> {
        val currentPlay = getCurrentPlaysUseCase.getCurrentPlayUseCase(
            userId = UUID.fromString("e120807e-32b7-4284-914d-3638f5bf3c08"),
            playlistId = playlistId,
        )
        return ApiResponseDTO.Success(
            message = "get current play success",
            data = currentPlay.toResponseDTO()
        )
    }
}