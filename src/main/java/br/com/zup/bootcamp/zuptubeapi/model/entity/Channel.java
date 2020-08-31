package br.com.zup.bootcamp.zuptubeapi.model.entity;

import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateAccountRequest;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Channel {

    private UUID id;
    private String name;
    private String description;
    private UUID accountId;
    private boolean removed;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    
    public Channel(final UUID accountId, final CreateAccountRequest request) {
        this.name = String.format("%s %s", request.getFirstName(), request.getLastName());
        this.description = String.format("Welcome to the Zuptube Channel of %s!", this.getName());
        this.accountId = accountId;
    }
}
