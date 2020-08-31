package br.com.zup.bootcamp.zuptubeapi.mock;

import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateVideoEngagementRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateVideoEngagementRequest;

public final class VideoEngagementMock {

    public VideoEngagementMock() {
        throw new IllegalStateException("Class not meant for instantiation.");
    }

    public static CreateVideoEngagementRequest buildCreateVideoEngagementRequest() {
        return CreateVideoEngagementRequest.builder()
                .comment("First one to comment!!11!!1!")
                .liked(true)
                .build();
    }

    public static UpdateVideoEngagementRequest buildUpdateVideoEngagementRequest() {
        return UpdateVideoEngagementRequest.builder()
                .comment("Khruangbin is the best band in the world!")
                .liked(false)
                .build();
    }

}
