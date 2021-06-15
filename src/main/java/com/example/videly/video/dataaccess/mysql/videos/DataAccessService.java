package com.example.videly.video.dataaccess.mysql.videos;

import com.example.videly.dao.VideoDAO;
import com.example.videly.video.Video;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("MySQLVideosRepository")
@AllArgsConstructor
public class DataAccessService implements VideoDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Video> findVideo(String name) {
        final String query = "SELECT * FROM videos WHERE name = ?";
        return jdbcTemplate.query(query, mapVideoFromDatabase(), name).stream().findFirst();
    }

    @Override
    public Optional<List<Video>> findVideosFromCategory(Long id) {
        final String query = "SELECT * FROM videos_categories WHERE category_id = ?";
        List<Long> videosIds = jdbcTemplate.query(query, mapLongColumnFromDatabase("video_id"), id);

        List<Video> filtered = new ArrayList<>();
        videosIds.forEach(videoId -> {
            final String query2 = "SELECT * FROM videos where id = ?";

            Optional<Video> video = jdbcTemplate.query(query2, mapVideoFromDatabase(), videoId).stream().findFirst();
            video.ifPresent(filtered::add);
        });

        return Optional.of(filtered);
    }

    private RowMapper<Long> mapLongColumnFromDatabase(String label) {
        return (rs, rowNum) -> rs.getLong(label);
    }



    @Override
    public Optional<Video> findVideo(Long Id) {
        final String query = "SELECT * FROM videos WHERE id = ?";
        return jdbcTemplate.query(query, mapVideoFromDatabase(), Id).stream().findFirst();
    }

    @Override
    public void setQuantity(Long id, int value) {
        final String query = "UPDATE videos SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(query, id, value);
    }

    @Override
    public Optional<List<Video>> listAllVideos() {
        final String query = "SELECT * FROM videos";
        return Optional.of(new ArrayList<>(jdbcTemplate.query(query, mapVideoFromDatabase())));
    }

    private RowMapper<Video> mapVideoFromDatabase() {
        return (resultSet, i) -> {
            final Long videoId = resultSet.getLong("id");
            final Integer quantity = resultSet.getInt("quantity");
            final String name = resultSet.getString("name");
            final String shortDescription = resultSet.getString("short_description");
            final String fullDescription = resultSet.getString("full_description");
            return new Video(videoId, name, shortDescription, fullDescription, quantity);
        };
    }
}
