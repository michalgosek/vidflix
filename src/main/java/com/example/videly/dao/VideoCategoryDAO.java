package com.example.videly.dao;

import com.example.videly.video.VideoCategory;

import java.util.List;
import java.util.Optional;

public interface VideoCategoryDAO {
    Optional<List<VideoCategory>> listAllCategories();
    Optional<VideoCategory> findCategory(Long id);
}
