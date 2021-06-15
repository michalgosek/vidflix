package com.example.videly.video.dataaccess.mysql.user;

import com.example.videly.dao.UserVideoDAO;
import com.example.videly.video.Video;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("MySQLUserVideosRepository")
@AllArgsConstructor
public class UserDataAccessService implements UserVideoDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<List<Video>> listVideos(String username) {
        final Long userId = findUserId(username);
        final String query = "SELECT * FROM users_videos WHERE user_id = ?";
        return Optional.of(new ArrayList<>(jdbcTemplate.query(query, mapVideoFromDatabase(), userId)));
    }

    @Override
    public Optional<Video> findVideo(Long videoId, String username) {
        final Long userId = findUserId(username);
        final String query = "SELECT * FROM users_videos WHERE user_id = ? AND video_id = ?";
        return jdbcTemplate.query(query, mapVideoFromDatabase(), userId, videoId).stream().findFirst();
    }

    @Override
    public void returnVideo(Long videoId, String username) {
        int quantity = getQuantityColumnValue(videoId);
        final Long userId = findUserId(username);
        jdbcTemplate.update("DELETE FROM users_videos WHERE user_id = ? AND video_id = ?", userId, videoId);
        jdbcTemplate.update("UPDATE videos SET quantity = ? WHERE id = ?", quantity + 1, videoId);
    }

    @Override
    public void insertVideo(Video video, String username) {
        int quantity = getQuantityColumnValue(video.getId());
        jdbcTemplate.update("INSERT INTO users_videos (user_id, video_id) VALUES (?, ?)", findUserId(username), video.getId());
        jdbcTemplate.update("UPDATE videos SET quantity = ? WHERE id = ?", quantity - 1, video.getId());
    }

    private Integer getQuantityColumnValue(Long id) {
        final String query2 = "SELECT quantity FROM videos WHERE id = ?";
        return jdbcTemplate.query(query2, mapQuantityColumn(), id).stream().findFirst().orElse(-1);
    }


    private Long findUserId(String username) {
        final String query = "SELECT id FROM users WHERE username = ?";
        return jdbcTemplate.query(query, mapIdColumn(), username).stream().findFirst().orElse(-1L);
    }

    private RowMapper<Integer> mapQuantityColumn() {
        return (rs, rowNum) -> rs.getInt("quantity");
    }

    private RowMapper<Long> mapIdColumn() {
        return (rs, rowNum) -> rs.getLong("id");
    }

    private RowMapper<Video> mapVideoFromDatabase() {
        return (resultSet, i) -> {
            final Long videoId = resultSet.getLong("video_id");
            return new Video(videoId);
        };
    }
}
