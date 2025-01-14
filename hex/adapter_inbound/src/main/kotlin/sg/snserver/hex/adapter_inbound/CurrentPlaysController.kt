package sg.snserver.hex.adapter_inbound

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import sg.snserver.hex.adapter_inbound.dto.*
import sg.snserver.hex.application.inbound.GetCurrentPlaysUseCase
import sg.snserver.hex.application.inbound.SaveCurrentPlayUseCase
import java.security.Principal
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

    @PreAuthorize("hasRole('USER')")
    @PostMapping(
        "/{playlistId}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun saveCurrentPlay(
        principal: Principal,
        @PathVariable playlistId: String,
        @RequestBody request: SaveCurrentPlayRequestDTO
    ): ApiResponseDTO.Success<Unit> {
        logger.info(principal.name)
        saveCurrentPlayUseCase.saveCurrentPlay(
            userId = UUID.fromString(principal.name),
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

    @PreAuthorize("hasRole('USER')")
    @PostMapping(
        "/{playlistId}/start-seconds",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun saveCurrentPlayStartSeconds(
        principal: Principal,
        @PathVariable playlistId: String,
        @RequestBody request: SaveCurrentPlayStartSecondsRequestDTO,
    ): ApiResponseDTO.Success<Unit> {
        logger.info(principal.name)
        saveCurrentPlayUseCase.setStartSeconds(
            userId = UUID.fromString(principal.name),
            playlistId = playlistId,
            startSeconds = request.startSeconds,
        )

        return ApiResponseDTO.emptySuccess(
            message = "set startSeconds in playlistId $playlistId"
        )
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    fun getCurrentPlays(
        principal: Principal,
    ): ApiResponseDTO.Success<GetCurrentPlayBatchResponseDTO> {
        logger.info(principal.name)
        val currentPlays = getCurrentPlaysUseCase.getCurrentPlayBatchUseCase(
            userId = UUID.fromString(principal.name),
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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{playlistId}")
    fun getCurrentPlay(
        principal: Principal,
        @PathVariable playlistId: String,
    ): ApiResponseDTO.Success<CurrentPlayResponseDTO> {
        logger.info(principal.name)
        val currentPlay = getCurrentPlaysUseCase.getCurrentPlayUseCase(
            userId = UUID.fromString(principal.name),
            playlistId = playlistId,
        )
        return ApiResponseDTO.Success(
            message = "get current play success",
            data = currentPlay.toResponseDTO()
        )
    }
}