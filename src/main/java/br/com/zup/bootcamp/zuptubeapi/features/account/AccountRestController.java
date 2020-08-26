package br.com.zup.bootcamp.zuptubeapi.features.account;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Account;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateAccountRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateAccountRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdatePasswordRequest;
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
@RequestMapping(value = "/api/v1/accounts", produces = APPLICATION_JSON_VALUE)
public class AccountRestController {

    private final AccountService accountService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Validated @RequestBody CreateAccountRequest request) {
        final UUID id = this.accountService.create(request);
        return created(URI.create(format("/api/v1/accounts/%s", id))).build();
    }

    @GetMapping
    public ResponseEntity<List<Account>> findAll() {
        return ok(this.accountService.findAll());
    }

    @GetMapping(value = "/{accountId}")
    public ResponseEntity<Optional<Account>> findById(@PathVariable UUID accountId) {
        return Optional.ofNullable(this.accountService.findById(accountId))
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @PutMapping(value = "/{accountId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable UUID accountId,
                                 @Validated @RequestBody UpdateAccountRequest request) {
        this.accountService.update(accountId, request);
        return noContent().build();
    }

    @PutMapping(value = "/{accountId}:update-password", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePassword(@PathVariable UUID accountId,
                                         @Validated @RequestBody UpdatePasswordRequest request) {
        this.accountService.updatePassword(accountId, request);
        return noContent().build();
    }

    @PostMapping(value = "/{accountId}:delete")
    public ResponseEntity<Void> deleteById(@PathVariable UUID accountId) {
        this.accountService.deleteOrUndeleteById(accountId, true);
        return noContent().build();
    }

    @PostMapping(value = "/{accountId}:undelete")
    public ResponseEntity<Void> undeleteById(@PathVariable UUID accountId) {
        this.accountService.deleteOrUndeleteById(accountId, false);
        return noContent().build();
    }
}
