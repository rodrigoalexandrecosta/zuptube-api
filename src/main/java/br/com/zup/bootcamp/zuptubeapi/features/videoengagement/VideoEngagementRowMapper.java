package br.com.zup.bootcamp.zuptubeapi.features.videoengagement;

import br.com.zup.bootcamp.zuptubeapi.model.entity.VideoEngagement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

public class VideoEngagementRowMapper implements RowMapper<VideoEngagement> {

    @Override
    public VideoEngagement mapRow(ResultSet rs, int rowNum) throws SQLException {
        return VideoEngagement.builder()
                .id(UUID.fromString(rs.getString("ID")))
                .comment(rs.getString("COMMENT"))
                .liked(rs.getBoolean("LIKED"))
                .accountId(UUID.fromString(rs.getString("ACCOUNT_ID")))
                .videoId(UUID.fromString(rs.getString("VIDEO_ID")))
                .removed(rs.getBoolean("REMOVED"))
                .createdAt(OffsetDateTime.from(rs.getObject("CREATED_AT", OffsetDateTime.class)))
                .updatedAt(OffsetDateTime.from(rs.getObject("UPDATED_AT", OffsetDateTime.class)))
                .build();
    }
}
