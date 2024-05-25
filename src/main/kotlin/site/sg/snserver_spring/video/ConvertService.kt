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
    fun convertToHLS(fileInputStream: InputStream, filenameNoExt: String) {
        val file = File(ClassLoader.getSystemResource("data/output").path, filenameNoExt)
        file.mkdir()

        FFmpeg.atPath()
            .addInput(
                PipeInput.pumpFrom(fileInputStream)
            ).addOutput(
                UrlOutput.toPath(Path(file.path, "index.m3u8"))
                    .setFormat("hls")
                    .addArguments("-hls_list_size", "0")
                    .addArguments("-hls_time", DURATION_SEGMENT_SECONDS.toString())
            )
            .setOverwriteOutput(true)
            .execute()
    }
}