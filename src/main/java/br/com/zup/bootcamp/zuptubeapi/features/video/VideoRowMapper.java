package br.com.zup.bootcamp.zuptubeapi.features.video;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Video;
import org.springframework.jdbc.core.RowMapper;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

public class VideoRowMapper implements RowMapper<Video> {

    @Override
    public Video mapRow(ResultSet rs, int rowNum) throws SQLException {
        Video video = Video.builder()
                .id(UUID.fromString(rs.getString("ID")))
                .title(rs.getString("TITLE"))
                .description(rs.getString("DESCRIPTION"))
                .filePath(rs.getString("FILE_PATH"))
                .channelId(UUID.fromString(rs.getString("CHANNEL_ID")))
                .removed(rs.getBoolean("REMOVED"))
                .createdAt(OffsetDateTime.from(rs.getObject("CREATED_AT", OffsetDateTime.class)))
                .updatedAt(OffsetDateTime.from(rs.getObject("UPDATED_AT", OffsetDateTime.class)))
                .build();

        video.setVideoFile(Paths.get(video.getFilePath()).toFile());
        return video;
    }
}
