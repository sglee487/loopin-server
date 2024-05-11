package site.sg.snserver_spring.video

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FileViewController {
    @GetMapping("/video", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getVideoFileBytes(): ByteArrayResource {
        val resource = ClassLoader.getSystemResource("data/test1.mp4") // Use ClassLoader directly
        return ByteArrayResource(FileCopyUtils.copyToByteArray(resource.openStream()))
    }

}