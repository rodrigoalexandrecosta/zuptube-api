package br.com.zup.bootcamp.zuptubeapi.mock;

import br.com.zup.bootcamp.zuptubeapi.exception.MultipartFileConversionErrorException;
import br.com.zup.bootcamp.zuptubeapi.model.entity.Video;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateVideoRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.VideoResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.VideoSearchResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class VideoMock {

    private VideoMock() {
        throw new IllegalStateException("Class not meant for instantiation.");
    }

    public static CreateVideoRequest buildCreateVideoRequest() {
        return CreateVideoRequest.builder()
                .title("Timewarp Inc - Yuil Disco Breaks (Soundsystem version) - Official Video")
                .description("Video clip for Yuil Disco Breaks (Soundsystem version) as featured in " +
                        "Timewarp Inc's latest album 'Theory of Revolution'")
                .build();
    }

    public static MultipartFile buildMultipartFileVideo() {
        final String filename = String.format("%s%s", "video", chooseRandomVideoExtension());
        return new MockMultipartFile(filename, "my-mocked-video".getBytes());
    }

    public static Video buildVideo() {
        return Video.builder()
                .build();
    }

    public static VideoResponse buildVideoResponse() {
        try {
            final String tempDir = String.format("%s/%s/%s", ".sample", "video-storage", UUID.randomUUID());
            new File(tempDir).mkdirs();
            return VideoResponse.builder()
                    .id(UUID.randomUUID())
                    .title("Dissolved Girl - Massive Attack")
                    .description("From the album 'Mezzanine'")
                    .videoFile(Files.write(Paths.get(String.format("%s/%s", tempDir, UUID.randomUUID())),
                            "my-video.mp4".getBytes()).toFile())
                    .createdAt(OffsetDateTime.now())
                    .build();
        } catch (IOException e) {
            throw new MultipartFileConversionErrorException("message.video.conversion.error", e); // Generic throw.
        }
    }

    public static VideoSearchResponse buildVideoSearchResponse() {
        return VideoSearchResponse.builder()
                .videoResponse(buildVideoResponse())
                .channelResponse(ChannelMock.buildChannelResponse())
                .build();
    }

    private static String chooseRandomVideoExtension() {
        final List<String> videoExtensions = List.of(".mp4", ".mkv", ".avi", ".flv", ".webm", ".ogv", ".mpeg", "rmvb");
        return videoExtensions.get(ThreadLocalRandom.current().nextInt(0, videoExtensions.size()));
    }
}
