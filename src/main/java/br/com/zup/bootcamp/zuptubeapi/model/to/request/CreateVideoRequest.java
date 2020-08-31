package br.com.zup.bootcamp.zuptubeapi.model.to.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateVideoRequest {

    @NotBlank(message = "message.video.title.mandatory")
    @Length(min = 1, max = 255, message = "message.video.title.length")
    private String title;

    @NotBlank(message = "message.video.description.mandatory")
    @Length(min = 1, max = 1000, message = "message.video.description.length")
    private String description;

    private String filePath;
}
