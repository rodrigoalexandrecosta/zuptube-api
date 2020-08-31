package br.com.zup.bootcamp.zuptubeapi.features.searchengine;

import br.com.zup.bootcamp.zuptubeapi.features.video.VideoService;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.VideoResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.VideoSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/searches", produces = APPLICATION_JSON_VALUE)
public class SearchEngineRestController {

    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<List<VideoSearchResponse>> searchVideo(@RequestParam(defaultValue = "", required = false) String search) {
        return ok(this.videoService.findBySearch(search));
    }

    @GetMapping(value = "/channels/{channelId}")
    public ResponseEntity<List<VideoResponse>> searchVideoInChannel(@RequestParam(defaultValue = "", required = false) String search,
                                                               @PathVariable UUID channelId) {
        return ok(this.videoService.findBySearchInChannel(channelId, search));
    }

}
