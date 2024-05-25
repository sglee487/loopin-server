package site.sg.snserver_spring.video

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File

@RestController
class VideoController(
    val convertService: ConvertService
) {

    @GetMapping("/video/{key}/{filename}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getVideoFileBytes(
        @PathVariable key: String,
        @PathVariable filename: String
    ): ResponseEntity<ByteArrayResource> {
        val file = File(ClassLoader.getSystemResource("data/output").path, "$key/$filename")

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/x-mpegURL"))
            .body(ByteArrayResource(FileCopyUtils.copyToByteArray(file)))
    }

    @GetMapping("/video/{key}/{resolution}/{filename}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getVideoFileBytes(
        @PathVariable key: String,
        @PathVariable resolution: String,
        @PathVariable filename: String
    ): ResponseEntity<ByteArrayResource> {
        val file = File(ClassLoader.getSystemResource("data/output").path, "$key/$resolution/$filename")

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/x-mpegURL"))
            .body(ByteArrayResource(FileCopyUtils.copyToByteArray(file)))
    }

    @PostMapping("/video")
    fun uploadVideoFile(@RequestParam("file") uploadedFile: MultipartFile) {
        val filenameNoExt = uploadedFile.originalFilename?.substringBeforeLast(".") ?: "video"
        convertService.convertToHLS(uploadedFile.inputStream, filenameNoExt)
    }


}