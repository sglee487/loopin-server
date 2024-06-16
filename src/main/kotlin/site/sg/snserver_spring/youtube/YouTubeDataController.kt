package site.sg.snserver_spring.youtube

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class YouTubeDataController(
    val youTubeDataService: YouTubeDataService
) {
    @GetMapping("/listitems")
    fun getItemList(
        @RequestParam playlistId: String
    ): PlayList {
        return youTubeDataService.getplaylist(playlistId)
    }
}