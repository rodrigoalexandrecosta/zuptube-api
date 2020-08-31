package br.com.zup.bootcamp.zuptubeapi.model.to.response;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Channel;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChannelResponse {

    private String name;
    private String description;

    public ChannelResponse(final Channel channel) {
        this.name = channel.getName();
        this.description = channel.getDescription();
    }
}
