package br.com.zup.bootcamp.zuptubeapi.features.video;

import br.com.zup.bootcamp.zuptubeapi.model.entity.Video;
import br.com.zup.bootcamp.zuptubeapi.model.to.request.CreateVideoRequest;
import br.com.zup.bootcamp.zuptubeapi.model.to.response.VideoSearchResponse;
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
public class VideoRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE = "INSERT INTO video " +
            "(title, description, file_path, channel_id) VALUES " +
            "(:title, :description, :filePath, :channelId) RETURNING id";

    private static final String FIND_BY_ID = "SELECT * FROM video " +
            "WHERE id = ? AND NOT removed";

    private static final String FIND_ALL_BY_CHANNEL_ID = "SELECT * FROM video " +
            "WHERE channel_id = ? AND NOT removed";

    private static final String FIND_BY_SEARCH = "SELECT * FROM video " +
            "JOIN channel on channel_id = channel.id " +
            "WHERE video.title ILIKE '%' || :param || '%' " +
            "OR video.description ILIKE '%' || :param || '%' " +
            "AND NOT video.removed";

    private static final String FIND_BY_SEARCH_IN_CHANNEL = "SELECT * FROM video " +
            "WHERE channel_id = :channelId " +
            "AND title ILIKE '%' || :param || '%' " +
            "OR description ILIKE '%' || :param || '%' " +
            "AND NOT removed";

    private static final String EXISTS = "SELECT exists(SELECT 1 FROM video " +
            "WHERE id = ?)";

    private static final String DELETE_UNDELETE = "UPDATE video SET " +
            "removed = :removed WHERE id = :id";

    UUID create(final UUID channelId, final CreateVideoRequest request) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", request.getTitle())
                .addValue("description", request.getDescription())
                .addValue("filePath", request.getFilePath())
                .addValue("channelId", channelId);

        return namedParameterJdbcTemplate.queryForObject(CREATE, params, UUID.class);
    }

    List<Video> findAllByChannelId(final UUID channelId) {
        return jdbcTemplate.query(FIND_ALL_BY_CHANNEL_ID, new VideoRowMapper(), channelId);
    }

    Optional<Video> findById(final UUID videoId) {
        return jdbcTemplate.query(FIND_BY_ID, new VideoRowMapper(), videoId)
                .stream()
                .findFirst();
    }

    List<VideoSearchResponse> findBySearch(final String search) {
        return namedParameterJdbcTemplate.query(FIND_BY_SEARCH,
                new MapSqlParameterSource("param", search),
                new VideoSearchResponseRowMapper());
    }

    List<Video> findBySearchInChannel(final UUID channelId, final String search) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("param", search)
                .addValue("channelId", channelId);

        return namedParameterJdbcTemplate.query(FIND_BY_SEARCH_IN_CHANNEL, params, new VideoRowMapper());
    }

    Boolean exists(final UUID videoId) {
        return jdbcTemplate.queryForObject(EXISTS, Boolean.class, videoId);
    }

    void deleteOrUndeleteById(final UUID videoId, final Boolean deleteOrUndelete) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("removed", deleteOrUndelete)
                .addValue("id", videoId);
        namedParameterJdbcTemplate.update(DELETE_UNDELETE, params);
    }


}
