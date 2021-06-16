package com.example.videly.video.dataaccess.dummy.categories;

import com.example.videly.dao.VideoCategoryDAO;
import com.example.videly.video.VideoCategory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("dummyCategoriesRepository")
public class DataAccessService implements VideoCategoryDAO {
    private final List<VideoCategory> categories;

    public DataAccessService() {
        categories = new ArrayList<>();
        categories.add(0, new VideoCategory(1L, "DRAMA"));
        categories.add(1, new VideoCategory(2L, "COMEDY"));
        categories.add(2, new VideoCategory(3L, "HORROR"));
    }

    @Override
    public Optional<VideoCategory> findCategory(Long id) {
        return categories.stream().filter(c -> id.equals(c.getId())).findFirst();
    }

    @Override
    public Optional<List<VideoCategory>> listAllCategories() {
        return Optional.of(categories);
    }
}
