package br.com.zup.bootcamp.zuptubeapi.features.subscription;

import br.com.zup.bootcamp.zuptubeapi.model.to.response.ChannelResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponseByAccount;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

public class SubscriptionResponseByAccountRowMapper implements RowMapper<SubscriptionResponseByAccount> {
    @Override
    public SubscriptionResponseByAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChannelResponse channelResponse = ChannelResponse.builder()
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .build();

        return SubscriptionResponseByAccount.builder()
                .id(UUID.fromString(rs.getString("ID")))
                .channelResponse(channelResponse)
                .createdAt(OffsetDateTime.from(rs.getObject("CREATED_AT", OffsetDateTime.class)))
                .build();
    }

}
