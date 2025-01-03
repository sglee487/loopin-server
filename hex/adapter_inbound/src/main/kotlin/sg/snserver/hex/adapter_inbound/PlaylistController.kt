package sg.snserver.hex.adapter_inbound

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import sg.snserver.hex.adapter_inbound.dto.ApiResponseDTO
import sg.snserver.hex.adapter_inbound.dto.CreatePlaylistRequestDTO
import sg.snserver.hex.adapter_inbound.dto.GetPlaylistResponseDTO
import sg.snserver.hex.adapter_inbound.dto.toResponseDTO
import sg.snserver.hex.application.inbound.GetPlaylistUseCase
import sg.snserver.hex.application.inbound.SaveYoutubeDataUseCase
import sg.snserver.hex.application.inbound.UpdateYoutubeDataUseCase

@RestController
@RequestMapping(
    "/api/v1/playlists",
    consumes = [],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@CrossOrigin(origins = ["http://localhost:14200", "http://localhost:6006"])
class PlaylistController(
    val saveYoutubeDataUseCase: SaveYoutubeDataUseCase,
    val getPlaylistUseCase: GetPlaylistUseCase,
    val updateYoutubeDataUseCase: UpdateYoutubeDataUseCase,
) {
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun createPlaylist(
        @RequestBody request: CreatePlaylistRequestDTO,
    ): ApiResponseDTO.Success<Unit> {

        saveYoutubeDataUseCase.savePlaylist(request.playlistId)

        return ApiResponseDTO.emptySuccess(
            message = "save successful",
        )
    }

    @GetMapping
    fun getPlaylists(
        pageable: Pageable,
    ): ApiResponseDTO.Success<Page<GetPlaylistResponseDTO>> {

        val playlistBatch = getPlaylistUseCase.getPlaylistBatch(pageable)

        return ApiResponseDTO.Success(
            message = "get playlistBatch success",
            data = playlistBatch.map {
                it.toResponseDTO()
            }
        )
    }

    @GetMapping(
        "/{playlistId}",
    )
    fun getPlaylist(
        @PathVariable playlistId: String,
    ): ApiResponseDTO.Success<GetPlaylistResponseDTO> {

        val playlist = getPlaylistUseCase.getPlaylist(playlistId)

        return ApiResponseDTO.Success(
            message = "get playlist success",
            data = playlist.toResponseDTO()
        )

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PatchMapping(
        "/{playlistId}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun updatePlaylist(
        @PathVariable playlistId: String,
    ): ApiResponseDTO.Success<Unit> {
        updateYoutubeDataUseCase.updatePlaylist(playlistId = playlistId)

        return ApiResponseDTO.emptySuccess(
            message = "update successful",
        )
    }
}