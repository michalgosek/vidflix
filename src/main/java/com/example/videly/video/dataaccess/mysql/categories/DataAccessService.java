package com.example.videly.video.dataaccess.mysql.categories;

import com.example.videly.dao.VideoCategoryDAO;
import com.example.videly.video.VideoCategory;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("MySQLCategoriesRepository")
@AllArgsConstructor
public class DataAccessService implements VideoCategoryDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<List<VideoCategory>> listAllCategories() {
        final String query = "SELECT * FROM categories";
        return Optional.of(jdbcTemplate.query(query, mapVideoCategoriesFromDatabase()));
    }

    private RowMapper<VideoCategory> mapVideoCategoriesFromDatabase() {
        return (resultSet, i) -> {
            final Long id = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            return new VideoCategory(id, name);
        };
    }

    @Override
    public Optional<VideoCategory> findCategory(Long id) {
        final String query = "SELECT * FROM categories where id = ?";
        return jdbcTemplate.query(query, mapVideoCategoriesFromDatabase(), id).stream().findFirst();
    }
}
