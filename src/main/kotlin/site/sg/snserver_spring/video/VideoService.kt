package site.sg.snserver_spring.video

import org.bson.types.Binary
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream

@Service
class VideoService(
    private val videoRepository: VideoRepository,
    private val convertService: ConvertService
) {

    fun getVideo(title: String, filename: String, resolution: String?): ByteArray? {
        val video = videoRepository.findByTitle(title)

        val filenameNoExt = filename.substringBeforeLast(".")

        resolution?.let {
            return when (it) {
                "480" -> video.res480p[filenameNoExt]?.data
                    ?: throw IllegalArgumentException("Segment not found $title $filename")

                "360" -> video.res360p[filenameNoExt]?.data
                    ?: throw IllegalArgumentException("Segment not found $title $filename")

                "240" -> video.res240p[filenameNoExt]?.data
                    ?: throw IllegalArgumentException("Segment not found $title $filename")

                else -> throw IllegalArgumentException("Invalid resolution $title $filename $resolution")
            }
        }

        return video.masterIndex.data
    }

    fun saveVideo(inputStream: InputStream, filenameNoExt: String) {
        val saveFilePath = convertService.convertToHLS(inputStream, filenameNoExt)
        val masterIndex = Binary(File(saveFilePath, "index.m3u8").readBytes())

        val res480p = File(saveFilePath, "480").listFiles()
            ?.associate { it.name.substringBeforeLast(".") to Binary(it.readBytes()) } ?: emptyMap()

        val res360p = File(saveFilePath, "360").listFiles()
            ?.associate { it.name.substringBeforeLast(".") to Binary(it.readBytes()) } ?: emptyMap()

        val res240p = File(saveFilePath, "240").listFiles()
            ?.associate { it.name.substringBeforeLast(".") to Binary(it.readBytes()) } ?: emptyMap()

        val video = Video(
            title = filenameNoExt,
            masterIndex = masterIndex,
            res480p = res480p,
            res360p = res360p,
            res240p = res240p
        )
        videoRepository.save(video)

    }
}