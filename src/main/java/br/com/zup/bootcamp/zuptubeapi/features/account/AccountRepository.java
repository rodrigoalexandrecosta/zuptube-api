package br.com.zup.bootcamp.zuptubeapi.features.account;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Account;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateAccountRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateAccountRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AccountRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private static final AccountRowMapper ROW_MAPPER = new AccountRowMapper();

    private static final String CREATE = "INSERT INTO account " +
            "(first_name, last_name, email, phone, password, locale, timezone) " +
            "VALUES " +
            "(:firstName, :lastName, :email, :phone, :password, :locale, :timezone) " +
            "RETURNING id";

    private static final String FIND_ALL = "SELECT * FROM account";

    private static final String FIND_BY_ID = "SELECT * FROM account " +
            "WHERE id = ? AND NOT removed";

    private static final String FIND_BY_EMAIL = "SELECT * FROM account " +
            "WHERE email = ? AND NOT removed";

    private static final String EXISTS = "SELECT exists(SELECT 1 FROM account" +
            " WHERE id = ?)";

    private static final String DELETE_UNDELETE = "UPDATE account SET " +
            "removed = ? WHERE id = ?";

    private static final String UPDATE = "UPDATE account SET " +
            "first_name = :firstName, last_name = :lastName, email = :email, " +
            "phone = :phone, locale = :locale, timezone = :timezone " +
            "WHERE id = :id";

    private static final String UPDATE_PASSWORD = "UPDATE account SET " +
            "password = ? WHERE id = ?";

    UUID create(final CreateAccountRequest request) {
        try {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("firstName", request.getFirstName())
                    .addValue("lastName", request.getLastName())
                    .addValue("email", request.getEmail())
                    .addValue("phone", request.getPhone())
                    .addValue("password", request.getPassword())
                    .addValue("locale", request.getLocale())
                    .addValue("timezone", request.getTimezone());

            return namedParameterJdbcTemplate.queryForObject(CREATE, params, UUID.class);
        } catch (DuplicateKeyException e) {
            validateDuplicateKeyException(e);
            throw e;
        }
    }

    List<Account> findAll() {
        return jdbcTemplate.query(FIND_ALL, ROW_MAPPER);
    }

    Optional<Account> findById(final UUID accountId) {
        return jdbcTemplate.query(FIND_BY_ID, ROW_MAPPER, accountId)
                .stream()
                .findFirst();
    }

    Optional<Account> findByEmail(String email) {
        return jdbcTemplate.query(FIND_BY_EMAIL, ROW_MAPPER, email)
                .stream()
                .findFirst();
    }

    Boolean exists(UUID accountId) {
        return jdbcTemplate.queryForObject(EXISTS, Boolean.class, accountId);
    }

    void update(final UUID accountId, final UpdateAccountRequest request) {
        try {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("firstName", request.getFirstName())
                    .addValue("lastName", request.getLastName())
                    .addValue("email", request.getEmail())
                    .addValue("phone", request.getPhone())
                    .addValue("locale", request.getLocale())
                    .addValue("timezone", request.getTimezone())
                    .addValue("id", accountId);

            namedParameterJdbcTemplate.update(UPDATE, params);
        } catch (DuplicateKeyException e) {
            validateDuplicateKeyException(e);
            throw e;
        }
    }

    void updatePassword(final UUID accountId, final String password) {
        jdbcTemplate.update(UPDATE_PASSWORD, password, accountId);
    }

    void deleteOrUndelete(final Boolean deleteOrUndelete, final UUID accountId) {
        jdbcTemplate.update(DELETE_UNDELETE, deleteOrUndelete, accountId);
    }

    private void validateDuplicateKeyException(DuplicateKeyException e) {
        final String errorMessage = Objects.requireNonNull(e.getRootCause()).getMessage();
        log.warn(errorMessage);
        if (errorMessage.contains("account_email_key")) {
            throw new DuplicateKeyException("message.account.email.unique");
        }
        if (errorMessage.contains("account_phone_key")) {
            throw new DuplicateKeyException("message.account.phone.unique");
        }
    }
}
