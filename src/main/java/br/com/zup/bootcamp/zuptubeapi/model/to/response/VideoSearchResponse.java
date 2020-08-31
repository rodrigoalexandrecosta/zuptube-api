package br.com.zup.bootcamp.zuptubeapi.model.to.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoSearchResponse {

    private VideoResponse videoResponse;
    private ChannelResponse channelResponse;
}
