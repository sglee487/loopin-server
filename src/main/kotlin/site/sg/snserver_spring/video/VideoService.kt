package site.sg.snserver_spring.video

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import java.util.*

@Service
class VideoService(
    private val videoRepository: VideoRepository,
    private val convertService: ConvertService,
    private val gridFsTemplate: GridFsTemplate,
    private val gridFsOperations: GridFsOperations
) {

    fun getVideo(uuid: String, filename: String, resolution: String?): GridFsResource {
        val video = videoRepository.findByUuid(uuid)
        val filenameNoExt = filename.substringBeforeLast(".")

        resolution?.let {
            return when (it) {
                "480" -> gridFsTemplate.getResource(
                    video.res480p[filenameNoExt] ?: throw IllegalArgumentException("Segment not found $uuid $filename")
                )

                "360" -> gridFsTemplate.getResource(
                    video.res360p[filenameNoExt] ?: throw IllegalArgumentException("Segment not found $uuid $filename")
                )

                "240" -> gridFsTemplate.getResource(
                    video.res240p[filenameNoExt] ?: throw IllegalArgumentException("Segment not found $uuid $filename")
                )

                else -> throw IllegalArgumentException("Invalid resolution $uuid $filename $resolution")
            }
        }
        return gridFsTemplate.getResource(video.masterIndex)
    }

    fun saveVideo(inputStream: InputStream, filenameNoExt: String, description: String?) {
        val uuid: String = UUID.randomUUID().toString()

        val saveFilePath = convertService.convertToHLS(inputStream, filenameNoExt)

        gridFsOperations.store(File(saveFilePath, "index.m3u8").inputStream(), "$uuid/index")
        val masterIndex = "$uuid/index"

        val res480p = File(saveFilePath, "480").listFiles()?.associate { file ->
            val name = file.name.substringBeforeLast(".")
            val savePath = "$uuid/480/$name"
            gridFsOperations.store(file.inputStream(), savePath)
            name to savePath
        } ?: emptyMap()

        val res360p = File(saveFilePath, "360").listFiles()?.associate { file ->
            val name = file.name.substringBeforeLast(".")
            val savePath = "$uuid/360/$name"
            gridFsOperations.store(file.inputStream(), savePath)
            name to savePath
        } ?: emptyMap()

        val res240p = File(saveFilePath, "240").listFiles()?.associate { file ->
            val name = file.name.substringBeforeLast(".")
            val savePath = "$uuid/240/$name"
            gridFsOperations.store(file.inputStream(), savePath)
            name to savePath
        } ?: emptyMap()

        val thumbnail = File(saveFilePath, "thumbnail.jpg")

        val videoInfo = readVideoInfoData(File(saveFilePath, "video_info.json"))

        val video = Video(
            title = filenameNoExt,
            uuid = uuid,
            masterIndex = masterIndex,
            res480p = res480p,
            res360p = res360p,
            res240p = res240p,
            thumbnail = Base64.getEncoder().encodeToString(thumbnail.readBytes()),
            videoInfo = videoInfo,
            author = "admin",
            description = description
        )
        videoRepository.save(video)
    }

    fun getVideos(page: Int, size: Int): Page<VideoDTO> {
        val pageable = PageRequest.of(page, size)
        return videoRepository.findAllProjectedBy(pageable)
    }

    private fun readVideoInfoData(file: File): VideoInfo {
        val gson = Gson()
        val jsonData = gson.fromJson(file.readText(), JsonObject::class.java)
        return VideoInfo(
            duration = jsonData.get("duration").asDouble
        )
    }
}