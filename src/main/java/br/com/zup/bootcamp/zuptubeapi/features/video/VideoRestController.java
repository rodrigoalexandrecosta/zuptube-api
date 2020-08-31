package br.com.zup.bootcamp.zuptubeapi.features.video;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Video;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateVideoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/channels", produces = APPLICATION_JSON_VALUE)
public class VideoRestController {

    private final VideoService videoService;

    @PostMapping(value = "/{channelId}/videos", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@PathVariable UUID channelId, @RequestParam(value = "video") MultipartFile video,
                                       @Validated @RequestBody CreateVideoRequest request) {
        final UUID id = this.videoService.create(channelId, video, request);
        return created(URI.create(format("/api/v1/videos/%s", id))).build();
    }

    @GetMapping(value = "/{channelId}/videos")
    public ResponseEntity<List<Video>> findAllByChannelId(@PathVariable UUID channelId) {
        return ok(this.videoService.findAllByChannelId(channelId));
    }

    @GetMapping(value = "/videos/{videoId}")
    public ResponseEntity<Optional<Video>> findById(@PathVariable UUID videoId) {
        return Optional.ofNullable(this.videoService.findById(videoId))
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @PostMapping(value = "/videos/{videoId}:delete")
    public ResponseEntity<Void> deleteById(@PathVariable UUID videoId) {
        this.videoService.deleteOrUndeleteById(videoId, true);
        return noContent().build();
    }

    @PostMapping(value = "/videos/{videoId}:undelete")
    public ResponseEntity<Void> undeleteById(@PathVariable UUID videoId) {
        this.videoService.deleteOrUndeleteById(videoId, false);
        return noContent().build();
    }
}
