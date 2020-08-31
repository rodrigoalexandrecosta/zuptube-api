package br.com.zup.bootcamp.zuptubeapi.features.videoengagement;

import br.com.zup.bootcamp.zuptubeapi.model.entity.VideoEngagement;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateVideoEngagementRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.UpdateVideoEngagementRequest;
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
public class VideoEngagementRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE = "INSERT INTO video_engagement " +
            "(comment, liked, account_id, video_id) VALUES " +
            "(:comment, :liked, :accountId, :videoId) RETURNING id";

    private static final String FIND_BY_ID = "SELECT * FROM video_engagement " +
            "WHERE id = ? AND NOT removed";

    private static final String FIND_BY_ACCOUNT_ID_AND_VIDEO_ID = "SELECT * FROM video_engagement " +
            "WHERE account_id = ? AND video_id = ? AND NOT removed";

    private static final String FIND_ALL_BY_VIDEO_ID = "SELECT * FROM video_engagement " +
            "WHERE video_id = ? AND NOT removed ORDER BY created_at DESC";

    private static final String FIND_ALL_BY_ACCOUNT_ID = "SELECT * FROM video_engagement " +
            "WHERE account_id = ? AND NOT removed ORDER BY created_at DESC";

    private static final String EXISTS_BY_ID = "SELECT exists(SELECT 1 FROM video_engagement WHERE id = ?)";

    private static final String EXISTS_BY_ACCOUNT_ID_AND_VIDEO_ID = "SELECT exists " +
            "(SELECT 1 FROM video_engagement WHERE account_id = ? AND video_id = ?)";

    private static final String UPDATE_BY_ID = "UPDATE video_engagement SET " +
            "comment = :comment, liked = :liked " +
            "WHERE id = :id";

    private static final String UPDATE_BY_ACCOUNT_ID_AND_VIDEO_ID = "UPDATE video_engagement SET " +
            "comment = :comment, liked = :liked " +
            "WHERE account_id = :accountId AND video_id = :videoId";

    UUID create(final UUID accountId, final UUID videoId, final CreateVideoEngagementRequest request) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("comment", request.getComment())
                .addValue("liked", request.getLiked())
                .addValue("accountId", accountId)
                .addValue("videoId", videoId);

        return namedParameterJdbcTemplate.queryForObject(CREATE, params, UUID.class);
    }

    Optional<VideoEngagement> findById(final UUID videoEngagementId) {
        return jdbcTemplate.query(FIND_BY_ID, new VideoEngagementRowMapper(), videoEngagementId)
                .stream()
                .findFirst();
    }

    Optional<VideoEngagement> findByAccountIdAndVideoId(final UUID accountId, final UUID videoId) {
        return jdbcTemplate.query(FIND_BY_ACCOUNT_ID_AND_VIDEO_ID, new VideoEngagementRowMapper(), accountId, videoId)
                .stream()
                .findFirst();
    }

    List<VideoEngagement> findAllByVideoId(final UUID videoId) {
        return jdbcTemplate.query(FIND_ALL_BY_VIDEO_ID, new VideoEngagementRowMapper(), videoId);
    }

    List<VideoEngagement> findAllByAccountId(final UUID accountId) {
        return jdbcTemplate.query(FIND_ALL_BY_ACCOUNT_ID, new VideoEngagementRowMapper(), accountId);
    }

    Boolean existsById(final UUID videoEngagementId) {
        return jdbcTemplate.queryForObject(EXISTS_BY_ID, Boolean.class, videoEngagementId);
    }

    Boolean existsByAccountIdAndVideoId(final UUID accountId, final UUID videoId) {
        return jdbcTemplate.queryForObject(EXISTS_BY_ACCOUNT_ID_AND_VIDEO_ID, Boolean.class, accountId, videoId);
    }

    void updateById(final UUID videoEngagementId, final UpdateVideoEngagementRequest request) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("comment", request.getComment())
                .addValue("liked", request.getLiked())
                .addValue("id", videoEngagementId);

        namedParameterJdbcTemplate.update(UPDATE_BY_ID, params);
    }

    void updateByAccountIdAndVideoId(final UUID accountId, final UUID videoId,
                                     final UpdateVideoEngagementRequest request) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("comment", request.getComment())
                .addValue("liked", request.getLiked())
                .addValue("accountId", accountId)
                .addValue("videoId", videoId);

        namedParameterJdbcTemplate.update(UPDATE_BY_ACCOUNT_ID_AND_VIDEO_ID, params);
    }
}
