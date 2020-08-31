package br.com.zup.bootcamp.zuptubeapi.features.channel;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Channel;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateChannelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/channels", produces = APPLICATION_JSON_VALUE)
public class ChannelRestController {

    private final ChannelService channelService;

    @GetMapping(value = "/{channelId}")
    public ResponseEntity<Optional<Channel>> findById(@PathVariable UUID channelId) {
        return Optional.ofNullable(this.channelService.findById(channelId))
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @GetMapping(value = "/{accountId}:find-channel")
    public ResponseEntity<Optional<Channel>> findByAccountId(@PathVariable UUID accountId) {
        return Optional.ofNullable(this.channelService.findByAccountId(accountId))
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Channel>> findAll() {
        return ok(this.channelService.findAll());
    }

    @PutMapping(value = "/{channelId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable UUID channelId,
                                       @Validated @RequestBody UpdateChannelRequest request) {
        this.channelService.updateById(channelId,request);
        return noContent().build();
    }

    @PutMapping(value = "/{accountId}:update-by-account", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateByAccountId(@PathVariable UUID accountId,
                                                  @Validated @RequestBody UpdateChannelRequest request) {
        this.channelService.updateByAccountId(accountId, request);
        return noContent().build();
    }

}
