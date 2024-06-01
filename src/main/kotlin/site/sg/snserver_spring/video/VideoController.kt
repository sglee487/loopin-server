package site.sg.snserver_spring.video

import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class VideoController(
    private val videoService: VideoService
) {

    @GetMapping("/video/{uuid}/{filename}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getVideoFileBytes(
        @PathVariable uuid: String,
        @PathVariable filename: String
    ): ResponseEntity<GridFsResource> {
        val fileBytes = videoService.getVideo(uuid, filename, null)

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/x-mpegURL"))
            .body(fileBytes)
    }

    @GetMapping("/video/{uuid}/{resolution}/{filename}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getVideoFileBytes(
        @PathVariable uuid: String,
        @PathVariable resolution: String?,
        @PathVariable filename: String
    ): ResponseEntity<GridFsResource> {
        val fileBytes = videoService.getVideo(uuid, filename, resolution)

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/x-mpegURL"))
            .body(fileBytes)
    }

    @PostMapping("/video")
    fun uploadVideoFile(@RequestParam("file") uploadedFile: MultipartFile, @RequestParam("content") content: String?) {
        val filenameNoExt = uploadedFile.originalFilename?.substringBeforeLast(".") ?: "video"
        videoService.saveVideo(uploadedFile.inputStream, filenameNoExt, content)
    }

}