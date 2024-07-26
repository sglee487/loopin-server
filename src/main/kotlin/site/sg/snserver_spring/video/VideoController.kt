package site.sg.snserver_spring.video

import org.springframework.data.domain.Page
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/video")
class VideoController(
    private val videoService: VideoService
) {

    @PostMapping
    fun uploadVideoFile(@RequestParam("file") uploadedFile: MultipartFile, @RequestParam("description") description: String?) {
        val filenameNoExt = uploadedFile.originalFilename?.substringBeforeLast(".") ?: "video"
        videoService.saveVideo(uploadedFile.inputStream, filenameNoExt, description)
    }

    @GetMapping("/{uuid}")
    fun getVideo(@PathVariable uuid: String): VideoDTO {
        return videoService.getVideo(uuid)
    }

    @GetMapping("/videos")
    fun getVideos(@RequestParam("page", defaultValue = "0") page: Int,
                  @RequestParam("size", defaultValue = "10") size: Int): Page<VideoDTO> {
        return videoService.getVideos(page, size)
    }

    @GetMapping("/video-hls/{uuid}/{filename}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getVideoFileBytes(
        @PathVariable uuid: String,
        @PathVariable filename: String
    ): ResponseEntity<GridFsResource> {
        val fileBytes = videoService.getVideoHLS(uuid, filename, null)

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/x-mpegURL"))
            .body(fileBytes)
    }

    @GetMapping("/video-hls/{uuid}/{resolution}/{filename}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getVideoFileBytes(
        @PathVariable uuid: String,
        @PathVariable resolution: String?,
        @PathVariable filename: String
    ): ResponseEntity<GridFsResource> {
        val fileBytes = videoService.getVideoHLS(uuid, filename, resolution)

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/x-mpegURL"))
            .body(fileBytes)
    }

}