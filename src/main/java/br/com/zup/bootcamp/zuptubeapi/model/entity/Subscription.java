package br.com.zup.bootcamp.zuptubeapi.model.entity;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Subscription {

    private UUID id;
    private UUID accountId;
    private UUID channelId;
    private boolean removed;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
