package site.sg.snserver_spring.video

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class YouTubeDataController(
    val youTubeDataService: YouTubeDataService
) {
    @GetMapping("/itemlist")
    fun getItemList() {
        youTubeDataService.getItemsList()
    }
}