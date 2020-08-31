package br.com.zup.bootcamp.zuptubeapi.model.to.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateChannelRequest {

    @NotBlank(message = "message.channel.name.mandatory")
    @Length(min = 1, max = 255, message = "message.channel.name.length")
    private String name;

    @NotBlank(message = "message.channel.description.mandatory")
    @Length(min = 1, max = 1000, message = "message.channel.description.length")
    private String description;
}
