package site.sg.snserver_spring.video

import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.Frame
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@RestController
class FileViewController {

    private final val DURATION_SEGMENT_SECONDS = 10 * 1000000

    @GetMapping("/video", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getVideoFileBytes(): ByteArrayResource {
        val resource = ClassLoader.getSystemResource("data/test1.mp4") // Use ClassLoader directly
        return ByteArrayResource(FileCopyUtils.copyToByteArray(resource.openStream()))
    }

    @PostMapping("/video")
    fun uploadVideoFile(@RequestParam("file") file: MultipartFile) {
        val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

        val tempFilename = file.originalFilename ?: "temp-video.mp4"
        val tempFile = File(System.getProperty("java.io.tmpdir"), tempFilename)
        file.transferTo(tempFile)
        println(tempFile)
        println(tempFile.path)

        splitVideo(tempFile.absolutePath, executor)
    }

    private fun splitVideo(filePath: String, executor: ExecutorService) {
        try {
            FFmpegFrameGrabber(filePath).use { grabber ->
                grabber.start()

                val totalDuration = grabber.lengthInTime
                val segmentLength = totalDuration / DURATION_SEGMENT_SECONDS

                for (segment in 0 .. segmentLength) {
                    val startTimestamp = segment * DURATION_SEGMENT_SECONDS
                    val endTimestamp =
                        if (segment == segmentLength) totalDuration else (segment + 1) * DURATION_SEGMENT_SECONDS

                    executor.submit {
                        try {
                            processSegment(filePath, segment, startTimestamp, endTimestamp)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun processSegment(filePath: String, segmentNumber: Long, startTimestamp: Long, endTimestamp: Long) {

        val tempFolder = File(System.getProperty("java.io.tmpdir"), "temp")
        if (!tempFolder.exists()) {
            tempFolder.mkdir()
        }

        val outputFileName = File(tempFolder, "segment_$segmentNumber.ts").absolutePath

        try {
            FFmpegFrameGrabber(filePath).use { grabber ->
                grabber.start()
                grabber.timestamp = startTimestamp
                FFmpegFrameRecorder(outputFileName, grabber.imageWidth, grabber.imageHeight).use { recorder ->
                    configureRecorder(recorder, grabber)
                    recorder.start()

                    var frame: Frame?
                    while ((grabber.grabFrame().also { frame = it }) != null && grabber.timestamp < endTimestamp) {
                        recorder.record(frame)
                    }

                    recorder.stop()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun configureRecorder(recorder: FFmpegFrameRecorder, grabber: FFmpegFrameGrabber) {
        recorder.format = "ts"
        recorder.frameRate = grabber.frameRate
        recorder.videoBitrate = grabber.videoBitrate
        recorder.videoCodec = avcodec.AV_CODEC_ID_H264
        recorder.pixelFormat = avutil.AV_PIX_FMT_YUV420P
        recorder.audioChannels = grabber.audioChannels
        recorder.sampleRate = grabber.sampleRate
        recorder.audioBitrate = grabber.audioBitrate
        recorder.audioCodec = grabber.audioCodec
    }

}