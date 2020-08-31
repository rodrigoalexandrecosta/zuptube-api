package br.com.zup.bootcamp.zuptubeapi.model.to.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionResponse {

    private UUID id;
    private AccountResponse accountResponse;
    private ChannelResponse channelResponse;
    private OffsetDateTime createdAt;
}
