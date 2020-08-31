package br.com.zup.bootcamp.zuptubeapi.model.entity;

import lombok.*;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Video {

    private UUID id;
    private String title;
    private String description;
    private String filePath;
    private UUID channelId;
    private File videoFile;
    private boolean removed;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
