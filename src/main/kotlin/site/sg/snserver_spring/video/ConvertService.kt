package site.sg.snserver_spring.video

import com.github.kokorin.jaffree.ffmpeg.FFmpeg
import com.github.kokorin.jaffree.ffmpeg.PipeInput
import com.github.kokorin.jaffree.ffmpeg.UrlOutput
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import kotlin.io.path.Path

@Service
class ConvertService {

    private final val DURATION_SEGMENT_SECONDS = 10

    // https://github.com/kokorin/Jaffree
    // https://velog.io/@haerong22/%EC%98%81%EC%83%81-%EC%8A%A4%ED%8A%B8%EB%A6%AC%EB%B0%8D-8.-%EC%8A%A4%ED%8A%B8%EB%A6%AC%EB%B0%8DHLS-%EC%9E%90%EB%8F%99-%ED%99%94%EC%A7%88-%EC%A1%B0%EC%A0%88
    fun convertToHLS(fileInputStream: InputStream, filenameNoExt: String) {
        val file = File(ClassLoader.getSystemResource("data/output").path, filenameNoExt)
        file.mkdir()

        val file480 = File(file.path, "480")
        file480.mkdir()

        val file360 = File(file.path, "360")
        file360.mkdir()

        val file240 = File(file.path, "240")
        file240.mkdir()

        FFmpeg.atPath()
            .addInput(
                PipeInput.pumpFrom(fileInputStream)
            ).addOutput(
                UrlOutput.toPath(Path(file.path, "/%v/index.m3u8"))
                    .setFormat("hls")
                    .addArguments("-hls_list_size", "0")
                    .addArguments("-hls_time", DURATION_SEGMENT_SECONDS.toString())
                    .addArguments("-master_pl_name", "index.m3u8")
                    .addArguments("-map", "0:v")
                    .addArguments("-map", "0:v")
                    .addArguments("-map", "0:v")
                    .addArguments("-var_stream_map", "v:0,name:480 v:1,name:360 v:2,name:240")

                    .addArguments("-b:v:0", "1000k")
                    .addArguments("-maxrate:v:0", "1000k")
                    .addArguments("-bufsize:v:0", "2000k")
                    .addArguments("-s:v:0", "640x480")
                    .addArguments("-crf:v:0", "28")
                    .addArguments("-b:a:0", "64k")

                    .addArguments("-b:v:1", "500k")
                    .addArguments("-maxrate:v:1", "500k")
                    .addArguments("-bufsize:v:1", "1000k")
                    .addArguments("-s:v:1", "480x320")
                    .addArguments("-crf:v:1", "28")
                    .addArguments("-b:a:1", "64k")

                    .addArguments("-b:v:2", "300k")
                    .addArguments("-maxrate:v:2", "300k")
                    .addArguments("-bufsize:v:2", "600k")
                    .addArguments("-s:v:2", "320x240")
                    .addArguments("-crf:v:2", "28")
            )
            .setOverwriteOutput(true)
            .execute()
    }
}