package br.com.zup.bootcamp.zuptubeapi.mock;

import br.com.zup.bootcamp.zuptubeapi.model.to.response.*;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class SubscriptionMock {

    private SubscriptionMock() {
        throw new IllegalStateException("Class not meant for instantiation.");
    }

    public static SubscriptionResponse buildSubscriptionResponse() {
        return SubscriptionResponse.builder()
                .id(UUID.randomUUID())
                .accountResponse(AccountMock.buildAccountResponse())
                .channelResponse(ChannelMock.buildChannelResponse())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public static SubscriptionResponseByChannel buildSubscriptionResponseByChannel() {
        return SubscriptionResponseByChannel.builder()
                .id(UUID.randomUUID())
                .accountResponse(AccountMock.buildAccountResponse())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public static SubscriptionResponseByAccount buildSubscriptionResponseByAccount() {
        return SubscriptionResponseByAccount.builder()
                .id(UUID.randomUUID())
                .channelResponse(ChannelMock.buildChannelResponse())
                .createdAt(OffsetDateTime.now())
                .build();
    }

}
