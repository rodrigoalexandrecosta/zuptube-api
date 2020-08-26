package br.com.zup.bootcamp.zuptubeapi.features.account;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Account;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

public class AccountRowMapper implements RowMapper <Account> {

    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Account.builder()
                .id(UUID.fromString(rs.getString("ID")))
                .firstName(rs.getString("FIRST_NAME"))
                .lastName(rs.getString("LAST_NAME"))
                .email(rs.getString("EMAIL"))
                .phone(rs.getString("PHONE"))
                .password(rs.getString("PASSWORD"))
                .locale(rs.getString("LOCALE"))
                .timezone(rs.getString("TIMEZONE"))
                .removed(rs.getBoolean("REMOVED"))
                .createdAt(OffsetDateTime.from(rs.getObject("CREATED_AT", OffsetDateTime.class)))
                .updatedAt(OffsetDateTime.from(rs.getObject("UPDATED_AT", OffsetDateTime.class)))
                .build();
    }
}