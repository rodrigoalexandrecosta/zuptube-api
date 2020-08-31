package br.com.zup.bootcamp.zuptubeapi.features.video;

import br.com.zup.bootcamp.zuptubeapi.exception.MultipartFileConversionErrorException;
import br.com.zup.bootcamp.zuptubeapi.exception.NotFoundException;
import br.com.zup.bootcamp.zuptubeapi.model.entity.Video;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateVideoRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.VideoResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.VideoSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final VideoRepository videoRepository;

    @Transactional
    public UUID create(final UUID channelId, final MultipartFile multipartVideo, final CreateVideoRequest request) {
        request.setFilePath(this.persistVideoFile(channelId, multipartVideo).toString());
        return this.videoRepository.create(channelId, request);
    }

    public Optional<Video> findById(final UUID videoId) {
        return this.videoRepository.findById(videoId)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("message.video.not-found"));
    }

    public List<Video> findAllByChannelId(final UUID channelId) {
        return this.videoRepository.findAllByChannelId(channelId);
    }

    public List<VideoSearchResponse> findBySearch(final String search) {
        return this.videoRepository.findBySearch(search);
    }

    public List<VideoResponse> findBySearchInChannel(final UUID channelId, final String search) {
        return this.videoRepository.findBySearchInChannel(channelId, search)
                .stream()
                .map(VideoResponse::new)
                .collect(Collectors.toList());
    }

    private boolean exists(final UUID videoId) {
        return this.videoRepository.exists(videoId);
    }

    @Transactional
    public void deleteOrUndeleteById(final UUID videoId, final Boolean deleteOrUndelete) {
        if (exists(videoId)) {
            this.videoRepository.deleteOrUndeleteById(videoId, deleteOrUndelete);
        } else {
            throw new NotFoundException("message.video.not-found");
        }
    }

    private Path persistVideoFile(final UUID channelId, final MultipartFile multipartVideo) {
        try {
            final String channelDirectory = this.getChannelDirectory(channelId);
            final String filename = this.generateFilename(multipartVideo.getName());
            File videoFile = new File(String.format("%s/%s", channelDirectory, filename));
            final byte[] bytes = multipartVideo.getBytes();
            return Files.write(videoFile.toPath(), bytes);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new MultipartFileConversionErrorException("message.video.conversion.error", e);
        }
    }

    private String getChannelDirectory(final UUID channelId) {
        final String directory = String.format("%s/%s/%s", ".sample", "video-storage", channelId);
        new File(directory).mkdirs(); // Creates channel dir if it does not exist.
        return directory;
    }

    private String generateFilename(final String multipartVideoName) {
        return String.format("%s%s",
                UUID.randomUUID().toString().replace("-", ""),
                this.getFileExtension(multipartVideoName).orElse(".mp4"));
    }

    private Optional<String> getFileExtension(final String multipartVideoName) {
        return Optional.ofNullable(multipartVideoName)
                .filter(videoName -> videoName.contains("."))
                .map(videoName -> videoName.substring(multipartVideoName.lastIndexOf(".")));
    }
}
