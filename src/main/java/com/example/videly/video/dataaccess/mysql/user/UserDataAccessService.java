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
        final String query = "SELECT * FROM user_videos WHERE user_id = ?";
        return Optional.of(new ArrayList<>(jdbcTemplate.query(query, mapVideoFromDatabase(), userId)));
    }

    @Override
    public Optional<Video> findVideo(Long videoId, String username) {
        final Long userId = findUserId(username);
        final String query = "SELECT * FROM user_videos WHERE user_id = ? AND video_id = ?";
        return jdbcTemplate.query(query, mapVideoFromDatabase(), userId, videoId).stream().findFirst();
    }

    @Override
    public void returnVideo(Long videoId, String username) {
        final Long userId = findUserId(username);
        final String query = "DELETE FROM user_videos WHERE user_id = ?, video_id = ?";
        jdbcTemplate.update(query, userId, videoId);
    }

    @Override
    public void insertVideo(Video video, String username) {
        final Long userId = findUserId(username);
        final String query = "INSERT INTO user_videos (user_id, video_id) VALUES (?, ?)";
        jdbcTemplate.update(query, userId, video.getId());
    }

    private Long findUserId(String username) {
        final String query = "SELECT id FROM users WHERE username = ?";
        return jdbcTemplate.query(query, mapUserIdFromDatabase(), username).stream().findFirst().orElse(-1L);
    }

    private RowMapper<Long> mapUserIdFromDatabase() {
        return (rs, rowNum) -> rs.getLong("user_id");
    }

    private RowMapper<Video> mapVideoFromDatabase() {
        return (resultSet, i) -> {
            final Long videoId = resultSet.getLong("video_id");
            return new Video(videoId);
        };
    }
}
