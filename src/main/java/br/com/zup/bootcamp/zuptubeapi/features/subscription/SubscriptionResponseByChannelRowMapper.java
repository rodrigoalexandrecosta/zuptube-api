package br.com.zup.bootcamp.zuptubeapi.features.subscription;

import br.com.zup.bootcamp.zuptubeapi.model.to.response.AccountResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponseByChannel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

public class SubscriptionResponseByChannelRowMapper implements RowMapper<SubscriptionResponseByChannel> {

    @Override
    public SubscriptionResponseByChannel mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountResponse accountResponse = AccountResponse.builder()
                .id(UUID.fromString(rs.getString("ACCOUNT_ID")))
                .firstName(rs.getString("FIRST_NAME"))
                .lastName(rs.getString("LAST_NAME"))
                .removed(rs.getBoolean("REMOVED"))
                .build();

        return SubscriptionResponseByChannel.builder()
                .id(UUID.fromString(rs.getString("ID")))
                .accountResponse(accountResponse)
                .createdAt(OffsetDateTime.from(rs.getObject("CREATED_AT", OffsetDateTime.class)))
                .build();
    }
}
