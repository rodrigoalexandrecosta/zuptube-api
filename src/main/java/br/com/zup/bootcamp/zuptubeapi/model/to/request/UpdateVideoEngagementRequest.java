package br.com.zup.bootcamp.zuptubeapi.model.to.request;

import jdk.jfr.BooleanFlag;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateVideoEngagementRequest {

    @Length(max = 2000, message = "message.video.engagement.comment.length")
    private String comment;

    @BooleanFlag
    private Boolean liked;
}
