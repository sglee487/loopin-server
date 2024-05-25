package site.sg.snserver_spring.video

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class VideoController(
    val convertService: ConvertService
) {

    @GetMapping("/video", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getVideoFileBytes(): ByteArrayResource {
        val resource = ClassLoader.getSystemResource("data/test1.mp4") // Use ClassLoader directly
        return ByteArrayResource(FileCopyUtils.copyToByteArray(resource.openStream()))
    }

    @PostMapping("/video")
    fun uploadVideoFile(@RequestParam("file") uploadedFile: MultipartFile) {
        val filenameNoExt = uploadedFile.originalFilename?.substringBeforeLast(".") ?: "video"
        convertService.convertToHLS(uploadedFile.inputStream, filenameNoExt)
    }


}