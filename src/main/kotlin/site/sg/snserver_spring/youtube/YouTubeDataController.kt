package site.sg.snserver_spring.youtube

import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class YouTubeDataController(
    val youTubeDataService: YouTubeDataService
) {
    @GetMapping("/listitems")
    fun getItemList(
        @RequestParam playlistId: String,
        @RequestParam refresh: Boolean = false
    ): PlayList {
        return youTubeDataService.getplaylist(playlistId, refresh)
    }

    @GetMapping("/lists")
    fun getPlayLists(
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): Page<ListsDTO> {
        return youTubeDataService.getPlayLists(page, size)
    }
}