package br.com.zup.bootcamp.zuptubeapi.mock;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Channel;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateChannelRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.ChannelResponse;

import java.util.UUID;

public final class ChannelMock {

    private ChannelMock() {
        throw new IllegalStateException("Class not meant for instantiation.");
    }

    public static UpdateChannelRequest buildUpdateChannelRequest() {
        return UpdateChannelRequest.builder()
                .name("3Blue1Brown")
                .description("3Blue1Brown, by Grant Sanderson, is some combination of " +
                        "math and entertainment, depending on your disposition.")
                .build();
    }

    public static Channel buildChannel() {
        return Channel.builder()
                .build();
    }

    public static ChannelResponse buildChannelResponse() {
        return ChannelResponse.builder()
                .name("PBS Space Time")
                .description("PBS Space Time explores the outer reaches of space, the craziness of astrophysics, " +
                        "the possibilities of sci-fi, and anything else you can think of beyond Planet Earth " +
                        "with our astrophysicist host: Matthew O’Dowd.")
                .build();
    }
}
