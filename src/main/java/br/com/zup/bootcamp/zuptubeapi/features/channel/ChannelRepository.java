package br.com.zup.bootcamp.zuptubeapi.features.channel;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Channel;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateChannelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ChannelRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE = "INSERT INTO channel " +
            "(name, description, account_id) VALUES " +
            "(:name, :description, :accountId) RETURNING id";

    private static final String FIND_BY_ID = "SELECT * FROM channel " +
            "WHERE id = ? AND NOT removed";

    private static final String FIND_BY_ACCOUNT_ID = "SELECT * FROM channel " +
            "WHERE account_id = ? AND NOT removed";

    private static final String FIND_ALL = "SELECT * FROM channel WHERE NOT removed";

    private static final String EXISTS_BY_ACCOUNT_ID = "SELECT exists(SELECT 1 FROM channel " +
            "WHERE account_id = ?)";

    private static final String DELETE_UNDELETE_BY_ACCOUNT_ID = "UPDATE channel SET " +
            "removed = :removed WHERE account_id = :accountId";

    private static final String UPDATE = "UPDATE channel SET " +
            "name = :name, description = :description " +
            "WHERE id = :channelId";

    private static final String UPDATE_BY_ACCOUNT_ID = "UPDATE channel SET " +
            "name = :name, description = :description " +
            "WHERE account_id = :accountId";

    UUID create(final Channel channel) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", channel.getName())
                .addValue("description", channel.getDescription())
                .addValue("accountId", channel.getAccountId());

        return namedParameterJdbcTemplate.queryForObject(CREATE, params, UUID.class);
    }

    Optional<Channel> findById(final UUID channelId) {
        return jdbcTemplate.query(FIND_BY_ID, new ChannelRowMapper(), channelId)
                .stream()
                .findFirst();
    }

    Optional<Channel> findByAccountId(final UUID accountId) {
        return jdbcTemplate.query(FIND_BY_ACCOUNT_ID, new ChannelRowMapper(), accountId)
                .stream()
                .findFirst();
    }

    List<Channel> findAll() {
        return jdbcTemplate.query(FIND_ALL, new ChannelRowMapper());
    }

    Boolean existsByAccountId(final UUID accountId) {
        return jdbcTemplate.queryForObject(EXISTS_BY_ACCOUNT_ID, Boolean.class, accountId);
    }

    void deleteOrUndeleteByAccountId(final UUID accountId, final Boolean deleteOrUndelete) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("removed", deleteOrUndelete)
                .addValue("accountId", accountId);

        namedParameterJdbcTemplate.update(DELETE_UNDELETE_BY_ACCOUNT_ID, params);
    }

    void updateById(final UUID channelId, final UpdateChannelRequest request) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", request.getName())
                .addValue("description", request.getDescription())
                .addValue("channelId", channelId);

        namedParameterJdbcTemplate.update(UPDATE, params);
    }

    void updateByAccountId(final UUID accountId, final UpdateChannelRequest request) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", request.getName())
                .addValue("description", request.getDescription())
                .addValue("accountId", accountId);

        namedParameterJdbcTemplate.update(UPDATE_BY_ACCOUNT_ID, params);
    }

}
