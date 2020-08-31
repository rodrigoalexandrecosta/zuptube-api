package br.com.zup.bootcamp.zuptubeapi.features.videoengagement;

import br.com.zup.bootcamp.zuptubeapi.model.entity.VideoEngagement;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateVideoEngagementRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateVideoEngagementRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/channels/videos", produces = APPLICATION_JSON_VALUE)
public class VideoEngagementRestController {

    private final VideoEngagementService videoEngagementService;

    @PostMapping(value = "/{videoId}/video-engagements/{accountId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@PathVariable UUID videoId, @PathVariable UUID accountId,
                                       @Validated @RequestBody CreateVideoEngagementRequest request) {
        final UUID id = this.videoEngagementService.create(accountId, videoId, request);
        return created(URI.create(format("/api/v1/videos/%s/video-engagements/%s/%s", videoId, accountId, id))).build();
    }

    @GetMapping(value = "/video-engagements/{videoEngagementId}")
    public ResponseEntity<Optional<VideoEngagement>> findById(@PathVariable UUID videoEngagementId) {
        return Optional.ofNullable(this.videoEngagementService.findById(videoEngagementId))
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @GetMapping(value = "/{videoId}/video-engagements/{accountId}")
    public ResponseEntity<Optional<VideoEngagement>> findByAccountIdAndVideoId(@PathVariable UUID accountId,
                                                                               @PathVariable UUID videoId) {
        return Optional.ofNullable(this.videoEngagementService.findByAccountIdAndVideoId(accountId, videoId))
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @GetMapping(value = "/{videoId}/video-engagements")
    public ResponseEntity<List<VideoEngagement>> findAllByVideoId(@PathVariable UUID videoId) {
        return ok(this.videoEngagementService.findAllByVideoId(videoId));
    }

    @GetMapping(value = "/video-engagements/{accountId}:find-by-account")
    public ResponseEntity<List<VideoEngagement>> findAllByAccountId(@PathVariable UUID accountId) {
        return ok(this.videoEngagementService.findAllByAccountId(accountId));
    }

    @PutMapping(value = "/video-engagements/{videoEngagementId}")
    public ResponseEntity<Void> updateById(@PathVariable UUID videoEngagementId,
                                           @Validated @RequestBody UpdateVideoEngagementRequest request) {
        this.videoEngagementService.updateById(videoEngagementId, request);
        return noContent().build();
    }

    @PutMapping(value = "/{videoId}/video-engagements/{accountId}")
    public ResponseEntity<Void> updateByAccountIdAndVideoId(@PathVariable UUID accountId, @PathVariable UUID videoId,
                                           @Validated @RequestBody UpdateVideoEngagementRequest request) {
        this.videoEngagementService.updateByAccountIdAndVideoId(accountId, videoId, request);
        return noContent().build();
    }
}
