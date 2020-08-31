package br.com.zup.bootcamp.zuptubeapi.model.entity;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoEngagement {

    private UUID id;
    private String comment;
    private boolean liked;
    private UUID accountId;
    private UUID videoId;
    private boolean removed;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
