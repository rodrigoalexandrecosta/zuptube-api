package br.com.zup.bootcamp.zuptubeapi.model.to.response;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Account;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private boolean removed;

    public AccountResponse(final Account account) {
        this.id = account.getId();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.removed = account.isRemoved();
    }
}
