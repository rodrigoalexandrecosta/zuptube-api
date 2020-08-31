package br.com.zup.bootcamp.zuptubeapi.features.subscription;

import br.com.zup.bootcamp.zuptubeapi.model.to.response.AccountResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.ChannelResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

public class SubscriptionResponseRowMapper implements RowMapper<SubscriptionResponse> {

    @Override
    public SubscriptionResponse mapRow(ResultSet rs, int rowNum) throws SQLException {

        AccountResponse account = AccountResponse.builder()
                .id(UUID.fromString(rs.getString("ACCOUNT_ID")))
                .firstName(rs.getString("FIRST_NAME"))
                .lastName(rs.getString("LAST_NAME"))
                .removed(rs.getBoolean("REMOVED"))
                .build();

        ChannelResponse channel = ChannelResponse.builder()
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .build();

        return SubscriptionResponse.builder()
                .id(UUID.fromString(rs.getString("ID")))
                .accountResponse(account)
                .channelResponse(channel)
                .createdAt(OffsetDateTime.from(rs.getObject("CREATED_AT", OffsetDateTime.class)))
                .build();
    }
}
