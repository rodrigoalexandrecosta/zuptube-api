package br.com.zup.bootcamp.zuptubeapi.features.subscription;

import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponse;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponseByAccount;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.SubscriptionResponseByChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE = "INSERT INTO subscription " +
            "(account_id, channel_id) VALUES (?, ?) RETURNING id";

    private static final String FIND = "SELECT * FROM subscription " +
            "JOIN account ON account_id = account.id " +
            "JOIN channel ON channel_id = channel.id " +
            "WHERE subscription.account_id = ? AND subscription.channel_id = ? AND NOT subscription.removed";

    private static final String FIND_ALL_BY_CHANNEL_ID = "SELECT * FROM subscription " +
            "JOIN account ON account_id = account.id " +
            "WHERE subscription.channel_id = ? AND NOT subscription.removed";

    private static final String FIND_ALL_BY_ACCOUNT_ID = "SELECT * FROM subscription " +
            "JOIN channel ON channel_id = channel.id " +
            "WHERE subscription.account_id = ? AND NOT subscription.removed";

    private static final String DELETE_UNDELETE = "UPDATE subscription SET " +
            "removed = ? WHERE account_id = ? AND channel_id = ?";

    UUID create(final UUID accountId, final UUID channelId) {
        return this.jdbcTemplate.queryForObject(CREATE, UUID.class, accountId, channelId);
    }

    Optional<SubscriptionResponse> find(final UUID accountId, final UUID channelId) {
        return this.jdbcTemplate.query(FIND, new SubscriptionResponseRowMapper(),accountId, channelId)
                .stream()
                .findFirst();
    }

    List<SubscriptionResponseByChannel> findAllByChannelId(final UUID channelId) {
        return this.jdbcTemplate.query(FIND_ALL_BY_CHANNEL_ID, new SubscriptionResponseByChannelRowMapper(), channelId);
    }

    List<SubscriptionResponseByAccount> findAllByAccountId(final UUID accountId) {
        return this.jdbcTemplate.query(FIND_ALL_BY_ACCOUNT_ID, new SubscriptionResponseByAccountRowMapper(), accountId);
    }

    void deleteOrUndelete(final UUID accountId, final UUID channelId, final Boolean deleteOrUndelete) {
        this.jdbcTemplate.update(DELETE_UNDELETE, deleteOrUndelete, accountId, channelId);
    }
}
