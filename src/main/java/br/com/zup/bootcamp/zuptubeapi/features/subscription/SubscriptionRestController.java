package br.com.zup.bootcamp.zuptubeapi.features.subscription;

import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponseByAccount;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponseByChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(value = "/api/v1/channels/subscriptions", produces = APPLICATION_JSON_VALUE)
public class SubscriptionRestController {

    private final SubscriptionService subscriptionService;

    @PostMapping(value = "/{channelId}/{accountId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@PathVariable UUID channelId, @PathVariable UUID accountId) {
        final UUID id = this.subscriptionService.create(accountId, channelId);
        return created(URI.create(format("/api/v1/channels/%s/%s/%s", channelId, accountId, id))).build();
    }

    @GetMapping(value = "/{channelId}/{accountId}")
    public ResponseEntity<Optional<SubscriptionResponse>> find(@PathVariable UUID channelId,
                                                               @PathVariable UUID accountId) {
        return Optional.ofNullable(this.subscriptionService.find(accountId, channelId))
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @GetMapping(value = "/{channelId}:find-by-channel")
    public ResponseEntity<List<SubscriptionResponseByChannel>> findAllByChannel(@PathVariable UUID channelId) {
        return ok(this.subscriptionService.findAllByChannelId(channelId));
    }

    @GetMapping(value = "/{accountId}:find-by-account")
    public ResponseEntity<List<SubscriptionResponseByAccount>> findAllByAccount(@PathVariable UUID accountId) {
        return ok(this.subscriptionService.findAllByAccountId(accountId));
    }

    @PostMapping(value = "/{channelId}/{accountId}:delete")
    public ResponseEntity<Void> delete(@PathVariable UUID channelId, @PathVariable UUID accountId) {
        this.subscriptionService.deleteOrUndelete(accountId, channelId, true);
        return noContent().build();
    }

    @PostMapping(value = "/{channelId}/{accountId}:undelete")
    public ResponseEntity<Void> undelete(@PathVariable UUID channelId, @PathVariable UUID accountId) {
        this.subscriptionService.deleteOrUndelete(accountId, channelId, false);
        return noContent().build();
    }
}
