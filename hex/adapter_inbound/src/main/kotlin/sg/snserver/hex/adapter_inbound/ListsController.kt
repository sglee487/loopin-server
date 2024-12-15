package sg.snserver.hex.adapter_inbound

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sg.snserver.hex.application.inbound.GetYoutubeDataUseCase

@RestController
@RequestMapping(
    "/api/v1/list",
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class ListsController(
    val getYoutubeDataUseCase: GetYoutubeDataUseCase,
) {
    @GetMapping("/item")
    fun getItemList(
        @RequestParam playlistId: String,
        @RequestParam refresh: Boolean = false
//    ): PlayListResponseDTO {
    ) {
        return getYoutubeDataUseCase.getPlaylist(playlistId, refresh)

    }
}