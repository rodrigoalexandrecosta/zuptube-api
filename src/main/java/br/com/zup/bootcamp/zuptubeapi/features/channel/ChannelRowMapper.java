package br.com.zup.bootcamp.zuptubeapi.features.channel;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Channel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ChannelRowMapper implements RowMapper<Channel> {

    @Override
    public Channel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Channel.builder()
                .id(UUID.fromString(rs.getString("ID")))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .accountId(UUID.fromString(rs.getString("ACCOUNT_ID")))
                .removed(rs.getBoolean("REMOVED"))
                .createdAt(OffsetDateTime.from(rs.getObject("CREATED_AT", OffsetDateTime.class)))
                .updatedAt(OffsetDateTime.from(rs.getObject("UPDATED_AT", OffsetDateTime.class)))
                .build();
    }
}
