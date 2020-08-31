package br.com.zup.bootcamp.zuptubeapi.features.video;

import br.com.zup.bootcamp.zuptubeapi.model.to.response.ChannelResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.VideoResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.VideoSearchResponse;
import org.springframework.jdbc.core.RowMapper;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

public class VideoSearchResponseRowMapper implements RowMapper<VideoSearchResponse> {

    @Override
    public VideoSearchResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChannelResponse channelResponse = ChannelResponse.builder()
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .build();

        VideoResponse videoResponse = VideoResponse.builder()
                .id(UUID.fromString(rs.getString("ID")))
                .title(rs.getString("TITLE"))
                .description("DESCRIPTION")
                .videoFile(Paths.get(rs.getString("FILE_PATH")).toFile())
                .createdAt(OffsetDateTime.from(rs.getObject("CREATED_AT", OffsetDateTime.class)))
                .build();

        return VideoSearchResponse.builder()
                .videoResponse(videoResponse)
                .channelResponse(channelResponse)
                .build();
    }
}
