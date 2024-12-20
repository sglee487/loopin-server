package sg.snserver.hex.adapter_inbound

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import sg.snserver.hex.adapter_inbound.dto.ApiResponseDTO
import sg.snserver.hex.adapter_inbound.dto.GetPlaylistResponseDTO
import sg.snserver.hex.adapter_inbound.dto.toResponseDTO
import sg.snserver.hex.application.inbound.GetPlaylistUseCase
import sg.snserver.hex.application.inbound.SaveYoutubeDataUseCase
import sg.snserver.hex.application.inbound.UpdateYoutubeDataUseCase

@RestController
@RequestMapping(
    "/api/v1/playlist",
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class PlaylistController(
    val saveYoutubeDataUseCase: SaveYoutubeDataUseCase,
    val getPlaylistUseCase: GetPlaylistUseCase,
    val updateYoutubeDataUseCase: UpdateYoutubeDataUseCase,
) {
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PostMapping
    fun createPlaylist(
        @RequestParam id: String,
        @RequestParam refresh: Boolean = false
    ): ApiResponseDTO.Success<Map<String, Any>> {

        saveYoutubeDataUseCase.savePlaylist(id)

        return ApiResponseDTO.emptySuccess(
            message = "save successful",
        )
    }

    @GetMapping("/batch")
    fun getPlaylistBatch(
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

    @GetMapping
    fun getItemList(
        @RequestParam id: String,
    ): ApiResponseDTO.Success<GetPlaylistResponseDTO> {

        val playlist = getPlaylistUseCase.getPlaylist(id)

        return ApiResponseDTO.Success(
            message = "get playlistBatch success",
            data = playlist.toResponseDTO()
        )

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PatchMapping("/{playlistId}")
    fun updateItem(
        @PathVariable playlistId: String,
    ): ApiResponseDTO.Success<Map<String, Any>> {
        updateYoutubeDataUseCase.updatePlaylist(playlistId = playlistId)

        return ApiResponseDTO.emptySuccess(
            message = "update successful",
        )
    }
}