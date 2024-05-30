package site.sg.snserver_spring.video

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class VideoController(
    private val videoService: VideoService
) {

    @GetMapping("/video/{title}/{filename}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getVideoFileBytes(
        @PathVariable title: String,
        @PathVariable filename: String
    ): ResponseEntity<ByteArray> {
        val fileBytes = videoService.getVideo(title, filename, null)
            ?: throw IllegalArgumentException("Video not found $title $filename")

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/x-mpegURL"))
            .body(fileBytes)
    }

    @GetMapping("/video/{title}/{resolution}/{filename}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getVideoFileBytes(
        @PathVariable title: String,
        @PathVariable resolution: String?,
        @PathVariable filename: String
    ): ResponseEntity<ByteArray> {
        val fileBytes = videoService.getVideo(title, filename, resolution)
            ?: throw IllegalArgumentException("Video not found $title $resolution $filename")

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/x-mpegURL"))
            .body(fileBytes)
    }

    @PostMapping("/video")
    fun uploadVideoFile(@RequestParam("file") uploadedFile: MultipartFile) {
        val filenameNoExt = uploadedFile.originalFilename?.substringBeforeLast(".") ?: "video"
        videoService.saveVideo(uploadedFile.inputStream, filenameNoExt)
    }


}