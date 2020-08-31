package br.com.zup.bootcamp.zuptubeapi.model.to.response;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Video;
import lombok.*;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoResponse {

    private UUID id;
    private String title;
    private String description;
    private File videoFile;
    private OffsetDateTime createdAt;

    public VideoResponse(final Video video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.description = video.getDescription();
        this.videoFile = video.getVideoFile();
        this.createdAt = video.getCreatedAt();
    }
}
